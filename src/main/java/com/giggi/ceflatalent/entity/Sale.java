package com.giggi.ceflatalent.entity;

import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "order_num", nullable = false)
    private Long orderNum; // ID_ORDER_NUM originale

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "revenues", precision = 15, scale = 4)
    private BigDecimal revenues;

    @Column(name = "cost", precision = 15, scale = 4)
    private BigDecimal cost;
}