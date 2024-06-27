package kz.project.techway.mapper;

import kz.project.techway.dto.input.CurrencyDTO;
import kz.project.techway.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(target = "id", ignore = true) // если id не нужен в dto
    Currency toEntity(CurrencyDTO dto);

    CurrencyDTO toDto(Currency entity);

    List<Currency> toEntityList(List<CurrencyDTO> dtoList);

    List<CurrencyDTO> toDtoList(List<Currency> entityList);
}
