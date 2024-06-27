package kz.project.techway.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Date;

@Getter
@Setter
@Builder
public class ConversationHistoryDTO {
    private Long userId;
    private Date fromDate;
    private Date toDate;
    private Sort.Direction sortDirection;
}
