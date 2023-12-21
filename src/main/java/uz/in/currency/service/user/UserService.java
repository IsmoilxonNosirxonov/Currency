package uz.in.currency.service.user;

import org.springframework.stereotype.Service;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.response.AuthenticationResponse;

@Service
public interface UserService {
    AuthenticationResponse save(UserCreateDto userCreateDto);

    AuthenticationResponse signIn(SignInDto signInDto);

}
