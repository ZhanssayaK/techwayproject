package kz.project.techway.service;

import kz.project.techway.dto.input.ConversationHistoryDTO;
import kz.project.techway.dto.output.ConversionHistoryResponseDTO;

import java.util.List;

public interface ConversionHistoryService {
    List<ConversionHistoryResponseDTO> getConversionHistoryByUser(ConversationHistoryDTO dto);
}
