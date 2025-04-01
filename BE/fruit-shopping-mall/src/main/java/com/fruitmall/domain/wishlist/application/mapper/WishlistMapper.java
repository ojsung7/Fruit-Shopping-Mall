package com.fruitmall.domain.wishlist.application.mapper;

import com.fruitmall.domain.wishlist.application.dto.WishlistDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistSummaryDto;
import com.fruitmall.domain.wishlist.domain.Wishlist;

import java.util.List;

public interface WishlistMapper {
    
    WishlistDto toDto(Wishlist wishlist);
    
    List<WishlistDto> toDtoList(List<Wishlist> wishlists);
    
    WishlistSummaryDto toSummaryDto(List<WishlistDto> wishlistItems);
}