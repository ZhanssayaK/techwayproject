package kz.project.techway.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.project.techway.dto.ConversationHistoryDTO;
import kz.project.techway.entity.ConversionHistory;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class HistorySpecifications {

    public Specification<ConversionHistory> querySearch(ConversationHistoryDTO dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = buildPredicates(dto, root, builder);

            applySorting(dto, query, root, builder);

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private List<Predicate> buildPredicates(ConversationHistoryDTO dto, Root<ConversionHistory> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(dto.getUserId())) {
            predicates.add(builder.equal(root.get("user").get("id"), dto.getUserId()));
        }

        if (Objects.nonNull(dto.getFromDate()) && Objects.nonNull(dto.getToDate())) {
            predicates.add(builder.between(root.get("conversionDateTime"), dto.getFromDate(), dto.getToDate()));
        }

        return predicates;
    }

    private void applySorting(ConversationHistoryDTO dto, CriteriaQuery<?> query, Root<ConversionHistory> root, CriteriaBuilder builder) {
        if (dto.getSortDirection() == Sort.Direction.DESC) {
            query.orderBy(builder.desc(root.get("conversionDateTime")));
        } else {
            query.orderBy(builder.asc(root.get("conversionDateTime")));
        }
    }
}
