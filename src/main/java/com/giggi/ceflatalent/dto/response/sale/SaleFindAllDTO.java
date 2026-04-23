package com.giggi.ceflatalent.dto.response.sale;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SaleFindAllDTO {
    private List<SaleFindDTO> SaleFindAllDTO;
}