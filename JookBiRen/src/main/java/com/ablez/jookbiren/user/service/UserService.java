package com.ablez.jookbiren.user.service;

import static com.ablez.jookbiren.answer.constant.AnswerConstant.SUSPECT;
import static com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import static com.ablez.jookbiren.security.utils.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;
import static com.ablez.jookbiren.utils.JookBiRenConstant.STAR_QUIZ_COUNT;

import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ExceptionCode;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtParseInterceptor jwtParseInterceptor;
    private final UserInfoService userInfoService;

    public LoginDto login(CodeDto codeInfo) {
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

    public void readExcel(MultipartFile file) {
        // 파일 Original 이름 불러오기
        String fileExtsn = FilenameUtils.getExtension(file.getOriginalFilename());
        System.out.println("fileExtsn = " + fileExtsn);

        Workbook workbook = null;
        try {
            // 엑셀 97-2003까지는 HSSF(xls), 엑셀 2007 이상은 XSSF(xlsx)
            if (fileExtsn.equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                workbook = new XSSFWorkbook(file.getInputStream());
            }

            // 엑셀파일에서 첫 번째 시트 불러오기
            Sheet worksheet = workbook.getSheetAt(0);

            // getPhysicalNumberOfRow : 행의 개수를 불러오는 메소드
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Row row = worksheet.getRow(i);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                if (row != null) {
                    for (int j = 0; j <= 9; j++) {
                        Cell cell = row.getCell(j);
                        String value = "";
                        if (cell == null) {
                            continue;
                        } else {
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        value = dateFormatter.format(cell.getDateCellValue());
                                    } else {
                                        double numericCellValue = cell.getNumericCellValue();
                                        value = String.valueOf(numericCellValue);
                                        if (numericCellValue == Math.rint(numericCellValue)) {
                                            value = String.valueOf((int) numericCellValue);
                                        } else {
                                            value = String.valueOf(numericCellValue);
                                        }
                                    }
                                    break;
                                case STRING:
                                    value = cell.getStringCellValue() + "";
                                    break;
                                case BLANK:
                                    value = cell.getBooleanCellValue() + "";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                                default:
                                    value = cell.getStringCellValue();
                                    break;
                            }
                        }

                        System.out.println(j + ": " + value);
                    }

                    workbook.close();
                }
            }

//            // 엑셀 파일 읽기
//            workbook = ExcelReader.readExcel(file.getInputStream(), fileExtsn);
//            // 엑셀 파일 데이터 저장
//            ExcelReader.saveExcelData(workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
