package kz.project.techway.service;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.*;
import kz.project.techway.entity.User;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    Logger logger = Logger.getLogger(AuthService.class.getName());

    public RegisterDTO register(RegisterRequest req) {
      User user = User.builder()
        .username(req.getUsername())
        .email(req.getEmail())
        .role(req.getRole())
        .password(req.getPassword())
        .build();

      User savedUser = userRepository.save(user);
      String token = tokenService.buildToken(savedUser);

      tokenService.save(
        savedUser.getUsername(),
        Token
          .builder()
          .creationDate(new Date())
          .isValid(true)
          .jwtExpiration(tokenService.extractExpiration(token))
          .userId(user.getId())
          .username(user.getUsername())
          .value(token)
          .build()
          .toString()
      );
      return new RegisterDTO(token);
    }

    public LoginDTO login(LoginRequest req) throws UserNotFound {
      Optional<User> userQuery = this.userRepository.findByUsername(req.getUsername());
      boolean isEmpty = userQuery.isEmpty();
      if (isEmpty) {
        throw new UserNotFound("User not found");
      }

      User user = userQuery.get();
      String token = tokenService.buildToken(user);
      tokenService.save(
              user.getUsername(),
              Token
                .builder()
                .creationDate(new Date())
                .isValid(true)
                .jwtExpiration(tokenService.extractExpiration(token))
                .userId(user.getId())
                .username(user.getUsername())
                .value(token)
                .build()
                .toString()
      );
      return new LoginDTO(token);
    }

    public void logout(HttpServletRequest request) {
        Iterator<String> it = request.getHeaderNames().asIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        final String authHeader = request.getHeader("authorization");
        final String jwt = authHeader.substring(7);
        tokenService.evictSingleCacheValue("tokenCache", tokenService.extractUsername(jwt));
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}