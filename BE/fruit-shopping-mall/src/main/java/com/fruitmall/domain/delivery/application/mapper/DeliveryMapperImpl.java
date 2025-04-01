package com.fruitmall.domain.delivery.application.mapper;

import com.fruitmall.domain.delivery.application.dto.AddressDto;
import com.fruitmall.domain.delivery.application.dto.AddressRequestDto;
import com.fruitmall.domain.delivery.application.dto.DeliveryDto;
import com.fruitmall.domain.delivery.domain.Address;
import com.fruitmall.domain.delivery.domain.Delivery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeliveryMapperImpl implements DeliveryMapper {

    @Override
    public DeliveryDto toDto(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        
        return DeliveryDto.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrder() != null ? delivery.getOrder().getId() : null)
                .orderNumber(delivery.getOrder() != null ? "ORD-" + delivery.getOrder().getId() : null) // 주문번호 포맷팅
                .deliveryStatus(delivery.getDeliveryStatus())
                .expectedDeliveryDate(delivery.getExpectedDeliveryDate())
                .actualDeliveryDate(delivery.getActualDeliveryDate())
                .courier(delivery.getCourier())
                .trackingNumber(delivery.getTrackingNumber())
                .address(toAddressDto(delivery.getAddress()))
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }

    @Override
    public List<DeliveryDto> toDtoList(List<Delivery> deliveries) {
        if (deliveries == null) {
            return null;
        }
        
        return deliveries.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        
        return AddressDto.builder()
                .recipient(address.getRecipient())
                .zipCode(address.getZipCode())
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .phoneNumber(address.getPhoneNumber())
                .deliveryRequest(address.getDeliveryRequest())
                .build();
    }

    @Override
    public Address toAddressEntity(AddressRequestDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        
        return Address.builder()
                .recipient(addressDto.getRecipient())
                .zipCode(addressDto.getZipCode())
                .address1(addressDto.getAddress1())
                .address2(addressDto.getAddress2())
                .phoneNumber(addressDto.getPhoneNumber())
                .deliveryRequest(addressDto.getDeliveryRequest())
                .build();
    }
}