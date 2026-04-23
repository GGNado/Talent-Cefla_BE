package com.giggi.ceflatalent.dto.response.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CustomerFindAllDTO {
    private List<CustomerFindDTO> CustomerFindAllDTO;
}