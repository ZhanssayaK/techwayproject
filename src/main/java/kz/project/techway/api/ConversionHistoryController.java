package kz.project.techway.api;

import jakarta.validation.constraints.NotNull;
import kz.project.techway.dto.input.ConversationHistoryDTO;
import kz.project.techway.dto.output.ConversionHistoryResponseDTO;
import kz.project.techway.service.ConversionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class ConversionHistoryController {
    private final ConversionHistoryService conversionHistoryService;

    @GetMapping
    public List<ConversionHistoryResponseDTO> getConversionHistoryByUser(@RequestParam @NotNull Long userId,
                                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                                         @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {
        ConversationHistoryDTO dto = ConversationHistoryDTO.builder()
                .userId(userId)
                .fromDate(fromDate)
                .toDate(toDate)
                .sortDirection(sortDirection)
                .build();
        return conversionHistoryService.getConversionHistoryByUser(dto);
    }
}
