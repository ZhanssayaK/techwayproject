package kz.project.techway.mapper;

import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.entity.CurrencyRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {
    CurrencyRateDTO toDto(CurrencyRate currencyRate);
    CurrencyRate toEntity(CurrencyRateDTO currencyRateDTO);
    List<CurrencyRateDTO> toDtoList(List<CurrencyRate> currencyRateList);
}
