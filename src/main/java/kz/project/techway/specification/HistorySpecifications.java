package kz.project.techway.specification;

import jakarta.persistence.criteria.Predicate;
import kz.project.techway.dto.ConversationHistoryDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class HistorySpecifications {

    public Specification<ConversionHistory> querySearch(ConversationHistoryDTO dto) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user").get("id"), dto.getUserId()));

            if (Objects.nonNull(dto.getFromDate()) && Objects.nonNull(dto.getToDate())) {
                predicates.add(builder.between(root.get("conversionDateTime"), dto.getFromDate(), dto.getToDate()));
            }

            query.orderBy(dto.getSortDirection() == Sort.Direction.DESC ? builder.desc(root.get("conversionDateTime")) :
                    builder.asc(root.get("conversionDateTime")));

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
}