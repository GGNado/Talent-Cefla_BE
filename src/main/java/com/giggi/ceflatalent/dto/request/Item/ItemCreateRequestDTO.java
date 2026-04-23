package com.giggi.ceflatalent.dto.request.Item;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemCreateRequestDTO {
    private String description;
    private Long businessLineId;
}