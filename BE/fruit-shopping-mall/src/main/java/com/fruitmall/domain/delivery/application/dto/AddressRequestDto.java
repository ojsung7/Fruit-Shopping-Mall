package com.fruitmall.domain.delivery.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
    
    @NotBlank(message = "수령인은 필수 입력값입니다")
    private String recipient;
    
    @NotBlank(message = "우편번호는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다")
    private String zipCode;
    
    @NotBlank(message = "주소는 필수 입력값입니다")
    private String address1;
    
    private String address2;
    
    @NotBlank(message = "연락처는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "연락처 형식이 올바르지 않습니다 (예: 010-1234-5678)")
    private String phoneNumber;
    
    private String deliveryRequest;
}