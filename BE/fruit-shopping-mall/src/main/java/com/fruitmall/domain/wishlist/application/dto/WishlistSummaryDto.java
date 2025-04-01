package com.fruitmall.domain.wishlist.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WishlistSummaryDto {
    
    private int totalItems;
    private List<WishlistDto> wishlistItems;
}