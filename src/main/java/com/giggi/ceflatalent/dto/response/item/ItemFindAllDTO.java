package com.giggi.ceflatalent.dto.response.item;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemFindAllDTO {
    private List<ItemFindDTO> ItemFindAllDTO;
}