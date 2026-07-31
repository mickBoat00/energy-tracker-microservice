package com.meichel.usage_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponse {
    private Long id;
    private String name;
    private String type;
    private Long userId;
}
