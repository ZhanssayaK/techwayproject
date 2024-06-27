package kz.project.techway.mapper;

import kz.project.techway.dto.output.UserResponseDTO;
import kz.project.techway.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto(User user);

    List<UserResponseDTO> toDtoList(List<User> user);
}
