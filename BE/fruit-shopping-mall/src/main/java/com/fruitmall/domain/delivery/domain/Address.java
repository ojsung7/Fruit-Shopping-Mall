package com.fruitmall.domain.delivery.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String address1;

    private String address2;

    @Column(nullable = false)
    private String phoneNumber;

    private String deliveryRequest;

    @Builder
    public Address(String recipient, String zipCode, String address1, String address2, 
                  String phoneNumber, String deliveryRequest) {
        this.recipient = recipient;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.phoneNumber = phoneNumber;
        this.deliveryRequest = deliveryRequest;
    }
}