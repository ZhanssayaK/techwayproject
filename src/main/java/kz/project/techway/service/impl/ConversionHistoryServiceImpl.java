package kz.project.techway.service.impl;

import kz.project.techway.dto.input.ConversationHistoryDTO;
import kz.project.techway.dto.output.ConversionHistoryResponseDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.mapper.ConversionHistoryMapper;
import kz.project.techway.repository.ConversionHistoryRepository;
import kz.project.techway.service.ConversionHistoryService;
import kz.project.techway.specification.HistorySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionHistoryServiceImpl implements ConversionHistoryService {
    private final ConversionHistoryRepository conversionHistoryRepository;
    private final ConversionHistoryMapper conversionHistoryMapper;

    @Override
    public List<ConversionHistoryResponseDTO> getConversionHistoryByUser(ConversationHistoryDTO dto) {
        List<ConversionHistory> conversionHistoryList = conversionHistoryRepository.findAll(HistorySpecifications.querySearch(dto));
        return conversionHistoryMapper.toDtoList(conversionHistoryList);
    }
}