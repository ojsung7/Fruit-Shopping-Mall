package com.fruitmall.domain.cart.application.mapper;

import com.fruitmall.domain.cart.application.dto.CartDto;
import com.fruitmall.domain.cart.application.dto.CartSummaryDto;
import com.fruitmall.domain.cart.domain.Cart;

import java.util.List;

public interface CartMapper {
    
    CartDto toDto(Cart cart);
    
    List<CartDto> toDtoList(List<Cart> carts);
    
    CartSummaryDto toSummaryDto(List<CartDto> cartItems);
}