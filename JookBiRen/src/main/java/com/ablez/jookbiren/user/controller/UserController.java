package com.ablez.jookbiren.user.controller;

import com.ablez.jookbiren.security.jwt.JwtDto.TokenDto;
import com.ablez.jookbiren.security.utils.JwtHeaderUtilEnums;
import com.ablez.jookbiren.user.dto.UserDto;
import com.ablez.jookbiren.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDto.CodeDto codeInfo, HttpServletResponse response) {
        TokenDto token = userService.login(codeInfo);
        response.setHeader("Authorization",
                JwtHeaderUtilEnums.GRANT_TYPE.getValue() + token.getAccessToken().getAccessToken());
        response.setHeader("Refresh",
                JwtHeaderUtilEnums.GRANT_TYPE.getValue() + token.getRefreshToken().getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }
}
