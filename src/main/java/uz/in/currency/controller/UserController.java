package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<TokenDTO> signUp(@RequestBody UserDTO createDto) {
        return ResponseEntity.ok(userService.save(createDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenDTO> signIn(
            @RequestBody SignInDTO signInDto
    ) {
        return ResponseEntity.ok(userService.signIn(signInDto));
    }

}

