package com.app.vasyBus.service.city;

import java.util.List;

public interface CityService {
    List<String> getCitySuggestions(String query);
}