package kz.project.techway.mapper;

import kz.project.techway.dto.output.ConversionHistoryResponseDTO;
import kz.project.techway.entity.ConversionHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversionHistoryMapper {
    ConversionHistoryResponseDTO toDto(ConversionHistory entity);

    List<ConversionHistoryResponseDTO> toDtoList(List<ConversionHistory> entities);
}
