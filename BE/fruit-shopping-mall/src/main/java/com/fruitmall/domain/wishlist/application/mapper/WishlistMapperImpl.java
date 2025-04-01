package com.fruitmall.domain.wishlist.application.mapper;

import com.fruitmall.domain.wishlist.application.dto.WishlistDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistSummaryDto;
import com.fruitmall.domain.wishlist.domain.Wishlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public WishlistDto toDto(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        
        return WishlistDto.builder()
                .id(wishlist.getId())
                .memberId(wishlist.getMember() != null ? wishlist.getMember().getId() : null)
                .fruitId(wishlist.getFruit() != null ? wishlist.getFruit().getId() : null)
                .fruitName(wishlist.getFruit() != null ? wishlist.getFruit().getFruitName() : null)
                .fruitImageUrl(wishlist.getFruit() != null ? wishlist.getFruit().getImageUrl() : null)
                .fruitPrice(wishlist.getFruit() != null ? wishlist.getFruit().getPrice() : null)
                .stockQuantity(wishlist.getFruit() != null ? wishlist.getFruit().getStockQuantity() : null)
                .origin(wishlist.getFruit() != null ? wishlist.getFruit().getOrigin() : null)
                .season(wishlist.getFruit() != null ? wishlist.getFruit().getSeason() : null)
                .addedDate(wishlist.getAddedDate())
                .createdAt(wishlist.getCreatedAt())
                .updatedAt(wishlist.getUpdatedAt())
                .build();
    }

    @Override
    public List<WishlistDto> toDtoList(List<Wishlist> wishlists) {
        if (wishlists == null) {
            return null;
        }
        
        return wishlists.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WishlistSummaryDto toSummaryDto(List<WishlistDto> wishlistItems) {
        if (wishlistItems == null) {
            return WishlistSummaryDto.builder()
                    .totalItems(0)
                    .wishlistItems(List.of())
                    .build();
        }
        
        return WishlistSummaryDto.builder()
                .totalItems(wishlistItems.size())
                .wishlistItems(wishlistItems)
                .build();
    }
}