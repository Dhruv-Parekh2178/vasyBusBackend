package com.app.vasyBus.dtos;

import com.app.vasyBus.model.enums.BusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public class BusResponseDTO {

    private Long busId;
    private String busName;
    private String busNumber;
    private BusType busType;
    private Integer totalSeats;
    private String operatorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant createdAt;
}