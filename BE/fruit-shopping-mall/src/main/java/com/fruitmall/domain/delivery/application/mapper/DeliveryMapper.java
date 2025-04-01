package com.fruitmall.domain.delivery.application.mapper;

import com.fruitmall.domain.delivery.application.dto.AddressDto;
import com.fruitmall.domain.delivery.application.dto.AddressRequestDto;
import com.fruitmall.domain.delivery.application.dto.DeliveryDto;
import com.fruitmall.domain.delivery.domain.Address;
import com.fruitmall.domain.delivery.domain.Delivery;

import java.util.List;

public interface DeliveryMapper {
    
    DeliveryDto toDto(Delivery delivery);
    
    List<DeliveryDto> toDtoList(List<Delivery> deliveries);
    
    AddressDto toAddressDto(Address address);
    
    Address toAddressEntity(AddressRequestDto addressDto);
}