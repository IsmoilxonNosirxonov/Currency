package uz.in.currency.service;

import org.springframework.stereotype.Service;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.dto.TokenDTO;

@Service
public interface UserService {
    TokenDTO save(UserDTO userDto);

    TokenDTO signIn(SignInDTO signInDto);

}
