package uz.in.currency.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.in.currency.config.JwtService;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.DataNotFoundException;
import uz.in.currency.exception.DublicateValueException;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.mapper.UserMapper;
import uz.in.currency.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public TokenDTO save(UserDTO userDto) {
        logger.info("Entering save() method");
        if (!userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            String errorMessage = "This email already exists: " + userDto.getEmail();
            logger.error(errorMessage);
            throw new DublicateValueException(errorMessage);
        }
        logger.debug("No user found with email: {}, proceeding with save operation", userDto.getEmail());
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(userMapper.userDtoToUser(userDto));
        String jwtToken = jwtService.generateToken(user);
        logger.info("Exiting save() method");
        return new TokenDTO(jwtToken);
    }

    @Override
    public TokenDTO signIn(SignInDTO signInDto) {
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
        return new TokenDTO(jwtToken);
    }

}
