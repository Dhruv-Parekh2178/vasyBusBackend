package com.app.vasyBus.controller;

import com.app.vasyBus.dto.bus.BusRequestDTO;
import com.app.vasyBus.dto.bus.BusResponseDTO;
import com.app.vasyBus.utils.ApiResponse;
import com.app.vasyBus.service.bus.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BusController {

    private final BusService busService;

    @PostMapping("/add/bus")
    public ResponseEntity<ApiResponse<BusResponseDTO>> addBus(@Valid @RequestBody BusRequestDTO busRequestDTO){
        BusResponseDTO busResponseDTO = busService.addBus(busRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(busResponseDTO));
    }

    @GetMapping("/buses")
    public ResponseEntity<ApiResponse<List<BusResponseDTO>>> getAllBuses(){
        List<BusResponseDTO>  busResponseDTOS = busService.getAllBuses();
        return ResponseEntity.ok(ApiResponse.success( busResponseDTOS));
    }

    @GetMapping("/bus/{id}")
    public ResponseEntity<ApiResponse<BusResponseDTO>> getBusById(@PathVariable Long id){
        BusResponseDTO busResponseDTO = busService.getBusById(id);
        return ResponseEntity.ok(ApiResponse.success( busResponseDTO));
    }

    @PutMapping("/update/bus/{id}")
    public ResponseEntity<ApiResponse<BusResponseDTO>> updateBus(@PathVariable Long id , @Valid @RequestBody BusRequestDTO busRequestDTO){
        BusResponseDTO busResponseDTO = busService.updateBus(id,busRequestDTO);
        return ResponseEntity.ok(ApiResponse.success( busResponseDTO));
    }

    @DeleteMapping("/delete/bus/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBus(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(busService.deleteBus(id)));
    }

}
