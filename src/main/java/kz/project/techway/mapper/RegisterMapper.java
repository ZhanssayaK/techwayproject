package kz.project.techway.mapper;

import kz.project.techway.dto.input.RegisterRequestDTO;
import kz.project.techway.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    User toUser(RegisterRequestDTO request);
}
