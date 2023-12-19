package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserCreateDto createDto) {
        return ResponseEntity.ok(userService.save(createDto));
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(
            @RequestBody SignInDto signInDto
    ) {
        return ResponseEntity.ok(userService.signIn(signInDto));
    }

}

