package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import com.giggi.ceflatalent.entity.Sale;
import com.giggi.ceflatalent.dto.request.sale.SaleCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.sale.SaleUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.sale.SaleFindDTO;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "item.id", source = "itemId")
    Sale convert(SaleCreateRequestDTO dto);

    Sale convert(SaleUpdateRequestDTO dto);

    @Mapping(target = "company_name", source = "company.description")
    @Mapping(target = "customer", source = "customer.description")
    SaleFindDTO convert(Sale entity);

    List<SaleFindDTO> convert(List<Sale> entities);
}