package me.ilucah.audiovisualiser_service.controller;

import lombok.extern.slf4j.Slf4j;
import me.ilucah.audiovisualiser_service.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/token")
    public String token(Authentication authentication) {
        log.debug("Token requested for user: {}.", authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token granted: {}.", token);
        return token;
    }
}
