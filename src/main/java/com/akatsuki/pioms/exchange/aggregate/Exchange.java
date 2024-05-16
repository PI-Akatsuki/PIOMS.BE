package com.akatsuki.pioms.exchange.aggregate;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "exchange")
public class Exchange {
    @Id
    @Column(name = "exchange_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exchangeCode;

    @Column(name = "exchange_date")
    private LocalDateTime exchangeDate;

    @Column(name = "exchange_status")
    @Enumerated(EnumType.STRING)
    private EXCHANGE_STATUS exchangeStatus;

    @JoinColumn(name = "franchise_code")
    @ManyToOne
    private Franchise franchise;

    @OneToMany(mappedBy = "exchange")
    @ToString.Exclude
    List<ExchangeProduct> products;


}
