package kz.project.techway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationHistoryDTO {
    private Long userId;
    private Date fromDate;
    private Date toDate;
    private Sort.Direction sortDirection;
}
