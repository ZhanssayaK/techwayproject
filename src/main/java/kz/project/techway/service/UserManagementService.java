package kz.project.techway.service;

import kz.project.techway.entity.User;
import kz.project.techway.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

  private final UserRepository userRepository;

  public List<User> listUsers() {
      return userRepository.findAll();
  }
}