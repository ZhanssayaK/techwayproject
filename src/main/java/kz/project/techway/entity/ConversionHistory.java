package kz.project.techway.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class ConversionHistory extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_currency", nullable = false)
    private Currency fromCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_currency", nullable = false)
    private Currency toCurrency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "conversion_date_time", nullable = false)
    private LocalDateTime conversionDateTime;
}
