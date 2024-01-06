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
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.CommonException;
import uz.in.currency.mapper.UserMapper;
import uz.in.currency.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public User save(UserDTO userDto) {
        logger.info("Request to save user with param: {}", userDto);

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.error("This email already exists: {}", userDto.getEmail());
            throw new CommonException("This email already exists: " + userDto.getEmail());
        }

        if (userDto.getPassword() == null || userDto.getPassword().isEmpty() || userDto.getPassword().isBlank()) {
            logger.error("Password can not be null or empty");
            throw new IllegalArgumentException("Password can not be null or empty");
        }

        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);

        logger.info("Response to save user with object: {}", save);
        return save;
    }

    @Override
    public TokenDTO signIn(SignInDTO signInDto) {
        logger.info("Request to log in user with param: {}", signInDto);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword()));

        Optional<User> userOptional = userRepository.findByEmail(signInDto.getEmail());
        if (userOptional.isEmpty()) {
            logger.error("User not found this email: {}", signInDto.getEmail());
            throw new CommonException("User not found this email: " + signInDto.getEmail());
        }
        String jwtToken = jwtService.generateToken(userOptional.get());

        logger.info("Response to log in user with object: {}", new TokenDTO(jwtToken));
        return new TokenDTO(jwtToken);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByEmail(username);
//        if (userOptional.isEmpty()){
//            throw new UsernameNotFoundException("User not found");
//        }
//        return userOptional.get();
//    }
}
