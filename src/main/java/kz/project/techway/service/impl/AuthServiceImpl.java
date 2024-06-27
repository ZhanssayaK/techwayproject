package kz.project.techway.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.input.LoginRequestDTO;
import kz.project.techway.dto.input.RegisterRequestDTO;
import kz.project.techway.dto.output.AuthDTO;
import kz.project.techway.entity.User;
import kz.project.techway.enums.RoleEnum;
import kz.project.techway.exceptions.PasswordsDoNotMatchException;
import kz.project.techway.exceptions.UserExistsException;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.mapper.RegisterMapper;
import kz.project.techway.repository.UserRepository;
import kz.project.techway.service.AuthService;
import kz.project.techway.service.RedisService;
import kz.project.techway.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RedisService redisService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long tokenExpiration;

    @Override
    public AuthDTO register(RegisterRequestDTO request) {
        if (!request.getPassword().equals(request.getRetypePassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match.");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(RoleEnum.USER)
                .password(request.getPassword())
                .build();

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserExistsException("Username " + user.getUsername() + " already exists.");
        }

        User savedUser = userRepository.save(user);

        String accessToken = tokenService.generateAndSaveToken(savedUser);
        String refreshToken = tokenService.generateRefreshToken(accessToken);
        Date expirationDate = tokenService.extractExpiration(accessToken);

        redisService.saveRefreshToken(user.getUsername(), refreshToken, tokenExpiration);

        return AuthDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationDate(expirationDate)
                .build();
    }


    @Override
    public AuthDTO login(LoginRequestDTO req) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UserNotFound("Invalid username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String accessToken = tokenService.generateAndSaveToken(user);
        String refreshToken = tokenService.generateRefreshToken(accessToken);
        Date expirationDate = tokenService.extractExpiration(accessToken);

        redisService.saveRefreshToken(user.getUsername(), refreshToken, tokenService.getRefreshTokenExpiration());

        return AuthDTO.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationDate(expirationDate)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            tokenService.evictToken(jwt);
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public String refreshToken(String refreshToken) {
        String username = tokenService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFound("User not found"));
        return tokenService.generateAndSaveToken(user);
    }
}
