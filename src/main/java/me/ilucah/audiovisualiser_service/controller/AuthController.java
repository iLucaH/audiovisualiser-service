package me.ilucah.audiovisualiser_service.controller;

import lombok.extern.slf4j.Slf4j;
import me.ilucah.audiovisualiser_service.model.RegisterResolve;
import me.ilucah.audiovisualiser_service.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    /*
    * At least one lowercase letter ((?=.*[a-z]))
    * At least one uppercase letter ((?=.*[A-Z]))
    * At least one digit ((?=.*\\d))
    * At least one special character from @$!%*?&
    * Minimum 8 characters in length, maximum 18 in length ({8,18})
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,18}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static final int USERNAME_MIN_CHARS = 3;
    public static final int USERNAME_MAX_CHARS = 20;

    @Autowired
    public AuthController(TokenService tokenService, JdbcUserDetailsManager jdbcUserDetailsManager) {
        this.tokenService = tokenService;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        log.debug("Token requested for user: {}.", authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token granted: {}.", token);
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (username == null)
            return RegisterResolve.Status.INVALID_USERNAME_NULL.build();
        else if (username.length() < USERNAME_MIN_CHARS)
            return RegisterResolve.Status.INVALID_USERNAME_MIN_CHARS.build();
        else if (username.length() > USERNAME_MAX_CHARS)
            return RegisterResolve.Status.INVALID_USERNAME_MAX_CHARS.build();
        else if (password == null)
            return RegisterResolve.Status.INVALID_PASSWORD_NULL.build();
        else if (jdbcUserDetailsManager.userExists(username))
            return RegisterResolve.Status.USERNAME_TAKEN.build();
        else if (jdbcUserDetailsManager.userExists(username))
            return RegisterResolve.Status.PASSWORD_UNSAFE.build();
        else if (!pattern.matcher(password).matches())
            return RegisterResolve.Status.PASSWORD_UNSAFE.build();
        else {
            log.debug("Register initiated for user: {}.", username);
            jdbcUserDetailsManager.createUser(User.builder().username(username).password("{noop}" + password).roles("USER").build());
            return RegisterResolve.Status.SUCCESS.build();
        }
    }
}

