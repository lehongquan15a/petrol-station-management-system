package com.gasstation.managementsystem.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "transaction_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private Long time; //unix time

    @Column(nullable = false)
    private Double volume;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "card_id")
    @ToString.Exclude
    private Card card;//Thanh toán bằng thẻ nào


    @ManyToOne
    @JoinColumn(name = "pump_shift_id", nullable = false)
    @ToString.Exclude
    private PumpShift pumpShift;

    @OneToOne(mappedBy = "transaction")
    private Debt debt;

}
