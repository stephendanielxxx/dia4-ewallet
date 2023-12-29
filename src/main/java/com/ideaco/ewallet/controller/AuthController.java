package com.ideaco.ewallet.controller;

import com.ideaco.ewallet.dto.LoginDTO;
import com.ideaco.ewallet.exception.LoginException;
import com.ideaco.ewallet.response.LoginResponse;
import com.ideaco.ewallet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam("phone_number") String phoneNumber,
                                               @RequestParam("password") String password){
        LoginResponse loginResponse = new LoginResponse();
        try {
            LoginDTO loginDTO = authService.login(phoneNumber, password);

            loginResponse.setSuccess(true);
            loginResponse.setMessage("Login succeed");
            loginResponse.setErrorCode("");
            loginResponse.setData(loginDTO);
            return ResponseEntity.ok().body(loginResponse);
        } catch (LoginException e) {
            loginResponse.setSuccess(false);
            loginResponse.setMessage("Login failed");
            loginResponse.setErrorCode("001");
            return ResponseEntity.badRequest().body(loginResponse);
        }
    }
}
