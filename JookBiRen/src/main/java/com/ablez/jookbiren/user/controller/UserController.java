package com.ablez.jookbiren.user.controller;

import static com.ablez.jookbiren.user.dto.UserDto.CodeDto;

import com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import com.ablez.jookbiren.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody @Valid CodeDto codeInfo, HttpServletResponse response) {
    public ResponseEntity login(@RequestBody @Valid CodeDto codeInfo) {
//        LoginDto loginInfo = userService.login(codeInfo);
//        response.setHeader("Authorization",
//                JwtHeaderUtilEnums.GRANT_TYPE.getValue() + loginInfo.getToken().getAccessToken().getAccessToken());
//        response.setHeader("Refresh",
//                JwtHeaderUtilEnums.GRANT_TYPE.getValue() + loginInfo.getToken().getRefreshToken().getRefreshToken());
//        return new ResponseEntity(loginInfo.getEnding(), HttpStatus.OK);
        return new ResponseEntity(userService.login(codeInfo), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestHeader("Refresh") String refreshToken, HttpServletResponse response) {
        TokenDto token = userService.reissue(refreshToken);
        response.setHeader("Authorization", token.getAccessToken());
        response.setHeader("Refresh", token.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity canPickSuspect() {
        return new ResponseEntity(userService.canPickSuspect(), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity getUserInfo() {
        return new ResponseEntity(userService.getInfo(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid CodeDto codeInfo) {
        userService.register(codeInfo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/excel")
    public ResponseEntity registerExcel(@RequestParam("file") MultipartFile file) {
        userService.readExcel(file);
        return new ResponseEntity(HttpStatus.OK);
    }
}
