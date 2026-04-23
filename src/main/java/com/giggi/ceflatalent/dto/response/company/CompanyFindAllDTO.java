package com.giggi.ceflatalent.dto.response.company;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CompanyFindAllDTO {
    private List<CompanyFindDTO> CompanyFindAllDTO;
}