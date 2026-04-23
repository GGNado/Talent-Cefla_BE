package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.ceflatalent.entity.Customer;
import com.giggi.ceflatalent.dto.request.customer.CustomerCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.customer.CustomerUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.customer.CustomerFindDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer convert(CustomerCreateRequestDTO dto);

    Customer convert(CustomerUpdateRequestDTO dto);

    CustomerFindDTO convert(Customer entity);

    List<CustomerFindDTO> convert(List<Customer> entities);
}