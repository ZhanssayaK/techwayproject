package kz.project.techway.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "currency", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CurrencyRate> currencyRates;
}
