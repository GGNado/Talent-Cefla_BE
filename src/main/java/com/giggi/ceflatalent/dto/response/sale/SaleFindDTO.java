package com.giggi.ceflatalent.dto.response.sale;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaleFindDTO {
    private Long id;
    private String description;
    private String company_name;
    private Long orderNum;
    private String customer;

}