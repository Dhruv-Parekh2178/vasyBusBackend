package com.app.vasyBus.service.city;

import com.app.vasyBus.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final RouteRepository routeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CITY_CACHE_PREFIX = "city:suggest:";
    private static final Duration CITY_CACHE_TTL  = Duration.ofSeconds(3600);
    private static final int     MIN_QUERY_LENGTH  = 2;

    @Override
    public List<String> getCitySuggestions(String query) {
        if (query == null || query.trim().length() < MIN_QUERY_LENGTH) {
            return List.of();
                 }

        String cleanQuery = query.trim();
        String cacheKey   = CITY_CACHE_PREFIX + cleanQuery.toLowerCase();

        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return (List<String>)cached;
        }

        List<String> cities = routeRepository.findCitiesByQuery(cleanQuery);
        if (!cities.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, cities, CITY_CACHE_TTL);
        }

        return cities;
    }
}