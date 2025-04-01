package com.fruitmall.domain.delivery.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDto {
    
    private String recipient;
    private String zipCode;
    private String address1;
    private String address2;
    private String phoneNumber;
    private String deliveryRequest;
}