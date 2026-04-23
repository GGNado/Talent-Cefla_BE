package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;

import java.util.List;

import com.giggi.ceflatalent.entity.Company;
import com.giggi.ceflatalent.dto.request.company.CompanyCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.company.CompanyUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.company.CompanyFindDTO;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company convert(CompanyCreateRequestDTO dto);

    Company convert(CompanyUpdateRequestDTO dto);

    CompanyFindDTO convert(Company entity);

    List<CompanyFindDTO> convert(List<Company> entities);
}