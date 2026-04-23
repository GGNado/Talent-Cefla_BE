package com.giggi.ceflatalent.dto.response.areaManager;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AreaManagerFindAllDTO {
    private List<AreaManagerFindDTO> AreaManagerFindAllDTO;
}