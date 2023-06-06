package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.JwtFilter;
import com.ismailcet.employeemanagement.dto.PositionDto;
import com.ismailcet.employeemanagement.dto.converter.PositionDtoConverter;
import com.ismailcet.employeemanagement.dto.request.CreatePositionRequest;
import com.ismailcet.employeemanagement.entity.Position;
import com.ismailcet.employeemanagement.exception.AuthenticationNotFoundException;
import com.ismailcet.employeemanagement.exception.PositionNotFoundException;
import com.ismailcet.employeemanagement.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final JwtFilter jwtFilter;
    private final PositionDtoConverter positionDtoConverter;
    public PositionService(PositionRepository positionRepository, JwtFilter jwtFilter, PositionDtoConverter positionDtoConverter) {
        this.positionRepository = positionRepository;
        this.jwtFilter = jwtFilter;
        this.positionDtoConverter = positionDtoConverter;
    }

    public PositionDto createPosition(CreatePositionRequest createPositionRequest) {
        try{
            if(jwtFilter.isSupervisor()){
                Position position = Position.builder()
                                .name(createPositionRequest.getName())
                                .build();
                positionRepository.save(position);
                return positionDtoConverter.convert(position);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public PositionDto updatePositionById(CreatePositionRequest createPositionRequest, Integer id) {
        try{
            Position position =
                    positionRepository.findById(id).orElseThrow(()->new PositionNotFoundException("Position Id does not valid ! "));
            if(jwtFilter.isSupervisor()){
                position.setName(createPositionRequest.getName());
                positionRepository.save(position);
                return positionDtoConverter.convert(position);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<PositionDto> getAllPosition() {
        try{
            if(jwtFilter.isManager()){
                List<PositionDto> positions = positionRepository.findAll()
                        .stream().map(positionDtoConverter::convert)
                        .collect(Collectors.toList());
                return positions;
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public PositionDto getPositionById(Integer id) {
        try{
            Position position = positionRepository.findById(id)
                            .orElseThrow(()-> new PositionNotFoundException("Position Id does not valid ! "));

            if(jwtFilter.isManager()){
                return positionDtoConverter.convert(position);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
