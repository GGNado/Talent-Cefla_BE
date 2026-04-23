package com.giggi.ceflatalent.dto.response.businessLine;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BusinessLineFindAllDTO {
    private List<BusinessLineFindDTO> BusinessLineFindAllDTO;
}