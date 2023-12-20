package uz.in.currency.service.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.domain.exception.DublicateValueException;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.mapper.MyMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public AuthenticationResponse save(UserCreateDto userCreateDto) {
        logger.info("Entering save() method");
        if (userRepository.findByEmail(userCreateDto.getEmail()).isEmpty()) {
            logger.debug("No user found with email: {}, proceeding with save operation", userCreateDto.getEmail());
            if (userCreateDto.getPassword() == null || userCreateDto.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
            User user = userRepository.save(MyMapper.INSTANCE.userCreateDtoToUser(userCreateDto));
            String jwtToken = jwtService.generateToken(user);
            logger.info("Exiting save() method");
            return new AuthenticationResponse(jwtToken);
        } else {
            String erorMessage="This email already exists: " + userCreateDto.getEmail();
            logger.error(erorMessage);
            throw new DublicateValueException(erorMessage);
        }
    }

    @Override
    public AuthenticationResponse signIn(SignInDto signInDto) {
        logger.info("Entering signIn() method");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDto.getEmail(),
                        signInDto.getPassword()
                )
        );
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> {
                    logger.error("No user found with the provided email: {}", signInDto.getEmail());
                    return new DataNotFoundException("Invalid credentials");
                });
        String jwtToken = jwtService.generateToken(user);
        logger.info("Exiting signIn() method");
        return new AuthenticationResponse(jwtToken);
    }

}
