package com.app.vasyBus.controller.admin;

import com.app.vasyBus.dto.route.RouteRequestDTO;
import com.app.vasyBus.dto.route.RouteResponseDTO;
import com.app.vasyBus.utils.ApiResponse;
import com.app.vasyBus.service.route.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/add/route")
    public ResponseEntity<ApiResponse<RouteResponseDTO>> addRoute(@Valid @RequestBody RouteRequestDTO routeRequestDTO){
        RouteResponseDTO routeResponseDTO = routeService.addRoute(routeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(routeResponseDTO));
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<List<RouteResponseDTO>>> getAllRoutes(){
        List<RouteResponseDTO> routeResponseDTOS = routeService.getAllRoutes();
        return ResponseEntity.ok( ApiResponse.success( routeResponseDTOS));
    }

    @GetMapping("/route/{id}")
    public ResponseEntity<ApiResponse<RouteResponseDTO>> getRouteById(@PathVariable Long id){
        RouteResponseDTO routeResponseDTO = routeService.getRouteById(id);
        return ResponseEntity.ok(ApiResponse.success( routeResponseDTO));
    }


    @PutMapping("/update/route/{id}")
    public ResponseEntity<ApiResponse<RouteResponseDTO>> updateRoute(@PathVariable Long id ,@Valid @RequestBody RouteRequestDTO routeRequestDTO){
        RouteResponseDTO routeResponseDTO = routeService.updateRoute(id , routeRequestDTO);
        return ResponseEntity.ok(ApiResponse.success( routeResponseDTO));
    }

    @DeleteMapping("/delete/route/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRoute(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(routeService.deleteRoute(id)));
    }
}
