package kz.project.techway.service;

import kz.project.techway.dto.ConversationHistoryDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.repository.ConversionHistoryRepository;
import kz.project.techway.specification.HistorySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionHistoryService {

    private final ConversionHistoryRepository conversionHistoryRepository;

    public List<ConversionHistory> getConversionHistoryByUser(ConversationHistoryDTO dto) {
        return conversionHistoryRepository.findAll(HistorySpecifications.querySearch(dto));
    }
}