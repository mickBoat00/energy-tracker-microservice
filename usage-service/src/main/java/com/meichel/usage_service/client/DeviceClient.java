package com.meichel.usage_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.meichel.usage_service.dto.DeviceResponse;

@Component
public class DeviceClient {

    protected final RestClient restClient;
    

    protected DeviceClient(@Value("${device.service.base.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public DeviceResponse getDeviceById(Long id){
        return restClient.get()
                .uri("/api/v1/devices/{id}/", id)
                .retrieve()
                .body(DeviceResponse.class);
    }

    
}
