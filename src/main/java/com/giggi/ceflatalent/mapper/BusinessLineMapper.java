package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.ceflatalent.entity.BusinessLine;
import com.giggi.ceflatalent.dto.request.businessLine.BusinessLineCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.businessLine.BusinessLineUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.businessLine.BusinessLineFindDTO;

@Mapper(componentModel = "spring")
public interface BusinessLineMapper {

    BusinessLine convert(BusinessLineCreateRequestDTO dto);

    BusinessLine convert(BusinessLineUpdateRequestDTO dto);

    BusinessLineFindDTO convert(BusinessLine entity);

    List<BusinessLineFindDTO> convert(List<BusinessLine> entities);
}