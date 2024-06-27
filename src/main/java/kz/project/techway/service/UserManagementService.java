package kz.project.techway.service;

import kz.project.techway.dto.output.UserResponseDTO;

import java.util.List;

public interface UserManagementService {
    List<UserResponseDTO> listUsers();
}
