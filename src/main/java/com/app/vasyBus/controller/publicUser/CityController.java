package com.app.vasyBus.controller.publicUser;

import com.app.vasyBus.service.city.CityService;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CityController {

    private final CityService cityService;
    @GetMapping("/routes/cities")
    public ResponseEntity<ApiResponse<List<String>>> getCitySuggestions(
            @RequestParam String query) {

        List<String> cities = cityService.getCitySuggestions(query);
        return ResponseEntity.ok(ApiResponse.success(cities));
    }
}