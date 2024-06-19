package com.ablez.jookbiren.user.service;

import static com.ablez.jookbiren.answer.constant.AnswerConstant.SUSPECT;
import static com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import static com.ablez.jookbiren.security.utils.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;
import static com.ablez.jookbiren.utils.JookBiRenConstant.STAR_QUIZ_COUNT;

import com.ablez.jookbiren.buyer.dto.BuyerInfoDto.PostBuyerInfoDto;
import com.ablez.jookbiren.buyer.service.BuyerInfoService;
import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ExceptionCode;
import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.security.entity.Authority;
import com.ablez.jookbiren.security.interceptor.JwtParseInterceptor;
import com.ablez.jookbiren.security.jwt.JwtTokenizer;
import com.ablez.jookbiren.security.redis.repository.RefreshTokenRepository;
import com.ablez.jookbiren.security.redis.token.RefreshToken;
import com.ablez.jookbiren.security.utils.JwtHeaderUtilEnums;
import com.ablez.jookbiren.user.dto.UserDto.CodeDto;
import com.ablez.jookbiren.user.dto.UserDto.EndingDto;
import com.ablez.jookbiren.user.dto.UserDto.InfoDto;
import com.ablez.jookbiren.user.dto.UserDto.LoginDto;
import com.ablez.jookbiren.user.dto.UserDto.StatusDto;
import com.ablez.jookbiren.user.entity.UserEp01;
import com.ablez.jookbiren.user.entity.UserInfoEp01;
import com.ablez.jookbiren.user.repository.UserJpaRepository;
import com.ablez.jookbiren.user.repository.UserRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private static final int CODE_LENGTH = 10;
    private static final int EXCEL_COLUMN_LENGTH = 9;

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtParseInterceptor jwtParseInterceptor;
    private final UserInfoService userInfoService;
    private final BuyerInfoService buyerInfoService;

    public LoginDto login(CodeDto codeInfo) {
        System.out.println("codeInfo = " + codeInfo);
        UserInfoEp01 userInfo = userInfoService.findByCode(codeInfo.getCode());
        UserEp01 user = userInfo.getUser();

        user.updateFirstLoginTime();

        String userId = String.valueOf(user.getUserId());
        String accessToken = jwtTokenizer.generateAccessToken(userId);
        RefreshToken refreshToken = saveRefreshToken(userId);

        return new LoginDto(new TokenDto(accessToken, refreshToken.getRefreshToken()),
                new EndingDto(user.getAnswerTime() != null));
    }

    public TokenDto reissue(String refreshToken) {
        refreshToken = validateToken(refreshToken);
        String username = jwtTokenizer.getUsername(refreshToken);
        RefreshToken existedRefreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.equals(existedRefreshToken.getRefreshToken())) {
            return reissueToken(username);
        }

        throw new BusinessLogicException(ExceptionCode.INVALID_REFRESH_TOKEN);
    }

    public InfoDto getInfo() {
//        String code = jwtParseInterceptor.getAuthenticatedUsername();
        UserEp01 user = findByUserId(Long.parseLong(jwtParseInterceptor.getAuthenticatedUsername()));

        LocalDateTime firstLoginTime = user.getFirstLoginTime();
        LocalDateTime answerTime = user.getAnswerTime();
        Duration duration = Duration.between(firstLoginTime, answerTime);

        return new InfoDto(user.getScore(), duration.toMinutes(), user.getAnswerCount(), user.getSolvedQuizCount(),
                SUSPECT.get(user.getCriminal()));
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRepository.save(RefreshToken.of(
                username,
                jwtTokenizer.generateRefreshToken(username),
                REFRESH_TOKEN_EXPIRATION_TIME.getValue()
        ));
    }

    public UserEp01 findByCode(String code) {
        return userRepository.findByCode(code)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_USER_CODE));
    }

    public UserEp01 findByUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_USER_ID));
    }

    private String validateToken(String token) {
        if (!token.startsWith(JwtHeaderUtilEnums.GRANT_TYPE.getValue())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_TOKEN_TYPE);
        }
        return token.substring(7);
    }

    private TokenDto reissueToken(String username) {
        return new TokenDto(jwtTokenizer.generateAccessToken(username), saveRefreshToken(username).getRefreshToken());
    }

    public StatusDto canPickSuspect() {
        UserEp01 user = findByUserId(Long.parseLong(jwtParseInterceptor.getAuthenticatedUsername()));
        ;

        int answerStatus = user.getAnswerStatusCode();
        return new StatusDto(answerStatus == (Math.pow(2, STAR_QUIZ_COUNT) - 1));
    }

    public void register(CodeDto codeInfo) {
        UserEp01 newUser = new UserEp01(codeInfo.getCode());
        Authority authority = new Authority("ROLE_USER", newUser);
        newUser.addRole(authority);
        userJpaRepository.save(newUser);
    }

    // 랜덤 문자열(코드) 생성
    private String generateCode() {
        return RandomStringUtils.randomAlphanumeric(CODE_LENGTH);
    }

    public void generateBuyerAndOrderInfo(MultipartFile file) {
        // 연락처 실명 플랫폼 주문번호 닉네임 주소 구매가격 구매날짜 생성할코드수
        readExcel(file);
    }

    public void readExcel(MultipartFile file) {
        String fileExtension = findFileExtension(file);
        Workbook workbook = null;
        try {
            workbook = makeWorkbook(fileExtension, file);

            // 엑셀파일에서 첫 번째 시트 불러오기
            Sheet worksheet = workbook.getSheetAt(0);

            // 각 행 읽어 처리
            for (int rowIdx = 0; rowIdx < worksheet.getPhysicalNumberOfRows();
                 rowIdx++) { // getPhysicalNumberOfRow : 행의 개수를 불러오는 메소드
                Row row = worksheet.getRow(rowIdx);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                if (row != null) {
                    List<String> infos = processExcelData(dateFormatter, row);
                    PostBuyerInfoDto buyerInfo = PostBuyerInfoDto.builder()
                            .phone(infos.get(0))
                            .name(infos.get(1))
                            .platform(infos.get(2))
                            .nickname(infos.get(4))
                            .address(infos.get(5))
                            .build();
                    System.out.println("date: " + infos.get(7));
                    PostOrderInfoDto orderInfo = PostOrderInfoDto.builder()
                            .orderNumber(infos.get(3))
                            .amount(Integer.parseInt(infos.get(6)))
                            .platform(infos.get(2))
                            .createdAt(LocalDateTime.parse(infos.get(7),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .build();
                    buyerInfoService.insertBuyerInfo(Integer.parseInt(infos.get(8)), buyerInfo, orderInfo);
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findFileExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    private Workbook makeWorkbook(String fileExtension, MultipartFile file) throws IOException {
        if (fileExtension.equals("xls")) {
            return new HSSFWorkbook(file.getInputStream());
        } else {
            return new XSSFWorkbook(file.getInputStream());
        }
    }

    private List<String> processExcelData(SimpleDateFormat dateFormatter, Row row) {
        // 각 열의 데이터를 저장할 리스트
        List<String> infos = new ArrayList<>();
        // 각 열의 데이터를 읽기(연락처 실명 플랫폼 주문번호 닉네임 주소 구매가격 구매날짜 생성할코드수)
        for (int colIdx = 0; colIdx < EXCEL_COLUMN_LENGTH; colIdx++) {
            Cell cell = row.getCell(colIdx);
            String value = "";
            if (cell == null) {
                continue;
            } else {
//                value = processCellData(cell, dateFormatter);
                infos.add(processCellData(cell, dateFormatter));
            }

            // 연락처 실명 플랫폼 주문번호 닉네임 주소 구매가격 구매날짜 생성할코드수
            // BuyerInfo -> 연락처, 실명, 플랫폼, 닉네임, 주소
            // OrderInfo -> 주문번호, 구매가격, 플랫폼, 구매날짜
            // UserInfoEp01 -> 생성할 코드 수 -> UserEp01에도 추가

//            System.out.println(colIdx + ": " + value);
        }

        return infos;
    }

    private String processCellData(Cell cell, SimpleDateFormat dateFormatter) {
        if (cell.getCellType() == CellType.FORMULA) {
            return cell.getCellFormula();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return processNumericData(cell, dateFormatter);
        }
        if (cell.getCellType() == CellType.STRING || cell.getCellType() == CellType.BLANK
                || cell.getCellType() == CellType.ERROR) {
            return cell.getStringCellValue() + "";
        }
        return cell.getStringCellValue();
    }

    private String processNumericData(Cell cell, SimpleDateFormat dateFormatter) {
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            return dateFormatter.format(cell.getDateCellValue());
        } else {
            double numericCellValue = cell.getNumericCellValue();
            if (numericCellValue == Math.rint(numericCellValue)) {
                return String.valueOf((int) numericCellValue);
            } else {
                return String.valueOf(numericCellValue);
            }
        }
    }

    public UserEp01 makeUser(String code) {
        return new UserEp01(code);
    }
}
