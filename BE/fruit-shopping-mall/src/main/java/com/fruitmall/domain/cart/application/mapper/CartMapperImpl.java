package com.fruitmall.domain.cart.application.mapper;

import com.fruitmall.domain.cart.application.dto.CartDto;
import com.fruitmall.domain.cart.application.dto.CartSummaryDto;
import com.fruitmall.domain.cart.domain.Cart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        // 총 가격 계산 (단가 * 수량)
        BigDecimal totalPrice = cart.getFruit().getPrice().multiply(new BigDecimal(cart.getQuantity()));
        
        return CartDto.builder()
                .id(cart.getId())
                .memberId(cart.getMember() != null ? cart.getMember().getId() : null)
                .fruitId(cart.getFruit() != null ? cart.getFruit().getId() : null)
                .fruitName(cart.getFruit() != null ? cart.getFruit().getFruitName() : null)
                .fruitImageUrl(cart.getFruit() != null ? cart.getFruit().getImageUrl() : null)
                .fruitPrice(cart.getFruit() != null ? cart.getFruit().getPrice() : null)
                .quantity(cart.getQuantity())
                .totalPrice(totalPrice)
                .addedDate(cart.getAddedDate())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    @Override
    public List<CartDto> toDtoList(List<Cart> carts) {
        if (carts == null) {
            return null;
        }
        
        return carts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartSummaryDto toSummaryDto(List<CartDto> cartItems) {
        if (cartItems == null) {
            return CartSummaryDto.builder()
                    .totalItems(0)
                    .totalPrice(BigDecimal.ZERO)
                    .cartItems(List.of())
                    .build();
        }
        
        int totalItems = cartItems.stream()
                .mapToInt(CartDto::getQuantity)
                .sum();
                
        BigDecimal totalPrice = cartItems.stream()
                .map(CartDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        return CartSummaryDto.builder()
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .cartItems(cartItems)
                .build();
    }
}