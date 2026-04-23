package com.giggi.ceflatalent.dto.request.customer;

import com.giggi.ceflatalent.entity.Country;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerCreateRequestDTO {
    private String description;
    private Country country;
    private Long areaManagerId;
}