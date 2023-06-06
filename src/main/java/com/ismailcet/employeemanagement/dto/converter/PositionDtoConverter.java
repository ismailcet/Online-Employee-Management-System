package com.ismailcet.employeemanagement.dto.converter;


import com.ismailcet.employeemanagement.dto.PositionDto;
import com.ismailcet.employeemanagement.entity.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionDtoConverter {

    public PositionDto convert(Position position){
        return PositionDto.builder()
                .id(position.getId())
                .name(position.getName())
                .build();
    }

}
