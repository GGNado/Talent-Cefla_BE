package com.giggi.ceflatalent.dto.request.sale;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class SaleCreateRequestDTO {
    private Long companyId;
    private Long orderNum;
    private Long customerId;
    private Long itemId;
    private LocalDate orderDate;
    private LocalDate invoiceDate;
    private BigDecimal revenues;
    private BigDecimal cost;

}