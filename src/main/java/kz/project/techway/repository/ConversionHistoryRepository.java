package kz.project.techway.repository;

import kz.project.techway.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long>, JpaSpecificationExecutor<ConversionHistory> {
    List<ConversionHistory> findByUserIdOrderByConversionDateTimeDesc(Long id);
}