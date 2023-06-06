package com.ismailcet.employeemanagement.controller;

import com.ismailcet.employeemanagement.dto.PositionDto;
import com.ismailcet.employeemanagement.dto.request.CreatePositionRequest;
import com.ismailcet.employeemanagement.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/position")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/")
    public ResponseEntity<PositionDto> createPosition(@RequestBody CreatePositionRequest createPositionRequest){
        return new ResponseEntity<>(
                positionService.createPosition(createPositionRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionDto> updatePositionById(@RequestBody CreatePositionRequest createPositionRequest, @PathVariable Integer id){
        return ResponseEntity.ok(positionService.updatePositionById(createPositionRequest, id));
    }
    @GetMapping("/")
    public ResponseEntity<List<PositionDto>> getAllPosition(){
        return ResponseEntity.ok(positionService.getAllPosition());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable Integer id){
        return ResponseEntity.ok(positionService.getPositionById(id));
    }
}
