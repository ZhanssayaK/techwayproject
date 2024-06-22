package kz.project.techway.service;

import kz.project.techway.entity.User;
import kz.project.techway.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserManagementService {
  private final UserRepository repository;
  public List<User> listUsers() {
      return repository.findAll();
  }
}