package kz.project.techway.service.impl;

import kz.project.techway.dto.output.UserResponseDTO;
import kz.project.techway.mapper.UserMapper;
import kz.project.techway.repository.UserRepository;
import kz.project.techway.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDTO> listUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }
}