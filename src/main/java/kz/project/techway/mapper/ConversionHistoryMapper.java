package kz.project.techway.mapper;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.output.ConversionHistoryResponseDTO;
import kz.project.techway.entity.ConversionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversionHistoryMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "fromCurrency.code", target = "fromCurrency.code"),
            @Mapping(source = "toCurrency.code", target = "toCurrency.code")
    })
    ConversionHistoryResponseDTO toDto(ConversionHistory entity);

    List<ConversionHistoryResponseDTO> toDtoList(List<ConversionHistory> entities);
}

