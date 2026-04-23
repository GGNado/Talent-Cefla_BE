package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.ceflatalent.entity.Item;
import com.giggi.ceflatalent.dto.request.Item.ItemCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.Item.ItemUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.item.ItemFindDTO;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item convert(ItemCreateRequestDTO dto);

    Item convert(ItemUpdateRequestDTO dto);

    ItemFindDTO convert(Item entity);

    List<ItemFindDTO> convert(List<Item> entities);
}