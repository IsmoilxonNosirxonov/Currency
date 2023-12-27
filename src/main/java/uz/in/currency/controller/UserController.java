package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.entity.User;
import uz.in.currency.service.UserService;
import uz.in.currency.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody UserDTO createDto) {
        logger.info("Request to sign up with param: {}", createDto);
        User response = userService.save(createDto);
        logger.info("Response to sign up with object: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenDTO> signIn(@RequestBody SignInDTO signInDto) {
        logger.info("Request to sign in with param: {}", signInDto);
        TokenDTO tokenDTO = userService.signIn(signInDto);
        logger.info("Response to sign in with object: {}", tokenDTO);
        return ResponseEntity.ok(tokenDTO);
    }

}

