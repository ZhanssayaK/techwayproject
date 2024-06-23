package kz.project.techway.mapper;

import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.entity.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(source = "currency.code", target = "currencyCode")
    CurrencyRateDTO toDto(CurrencyRate currencyRate);

    @Mapping(source = "currencyCode", target = "currency.code")
    CurrencyRate toEntity(CurrencyRateDTO currencyRateDTO);

    List<CurrencyRateDTO> toDtoList(List<CurrencyRate> currencyRateList);
}
