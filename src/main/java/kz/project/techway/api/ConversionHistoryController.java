package kz.project.techway.api;

import kz.project.techway.dto.ConversationHistoryDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.service.ConversionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class ConversionHistoryController {

    private final ConversionHistoryService conversionHistoryService;

    @GetMapping()
    public List<ConversionHistory> getConversionHistoryByUser(@RequestParam Long userId,
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
