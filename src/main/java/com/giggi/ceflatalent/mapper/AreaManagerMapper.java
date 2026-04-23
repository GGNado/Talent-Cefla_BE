package com.giggi.ceflatalent.mapper;

import org.mapstruct.Mapper;
import java.util.List;
import com.giggi.ceflatalent.entity.AreaManager;
import com.giggi.ceflatalent.dto.request.areaManager.AreaManagerCreateRequestDTO;
import com.giggi.ceflatalent.dto.request.areaManager.AreaManagerUpdateRequestDTO;
import com.giggi.ceflatalent.dto.response.areaManager.AreaManagerFindDTO;

@Mapper(componentModel = "spring")
public interface AreaManagerMapper {
    AreaManager convert(AreaManagerCreateRequestDTO dto);
    AreaManager convert(AreaManagerUpdateRequestDTO dto);
    AreaManagerFindDTO convert(AreaManager entity);
    List<AreaManagerFindDTO> convert(List<AreaManager> entities);
}