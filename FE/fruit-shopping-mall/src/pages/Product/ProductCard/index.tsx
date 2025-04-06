// src/components/product/ProductCard/index.tsx
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { addToCart } from '@/store/slices/cartSlice';
import { addToWishlist, removeFromWishlist } from '@/store/slices/wishlistSlice';
import { Link } from 'react-router-dom';
import { formatPrice } from '@/utils/formatters';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

interface ProductCardProps {
    product: {
        id: number;
        fruitName: string;
        price: number;
        imageUrl: string;
        origin: string;
        stockQuantity: number;
    };
}

const Card = styled.div`
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  }
`;

const ProductImage = styled.div`
  height: 200px;
  overflow: hidden;
  position: relative;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
  }
  
  &:hover img {
    transform: scale(1.05);
  }
`;

const WishlistButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  z-index: 1;
  font-size: 18px;
  color: ${props => props.color};
  
  &:hover {
    transform: scale(1.1);
  }
`;

const ProductInfo = styled.div`
  padding: 16px;
`;

const ProductName = styled(Link)`
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  display: block;
  color: #333;
  
  &:hover {
    color: #4caf50;
  }
`;

const ProductOrigin = styled.div`
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
`;

const ProductPrice = styled.div`
  font-size: 18px;
  font-weight: 700;
  color: #4caf50;
  margin-bottom: 16px;
`;

const ProductActions = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const AddToCartButton = styled.button`
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  font-weight: 500;
  cursor: pointer;
  flex: 1;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const StockStatus = styled.div`
  color: ${props => props.color};
  font-size: 14px;
  font-weight: 500;
  text-align: right;
`;

const ProductCard = ({ product }: ProductCardProps) => {
    const dispatch = useAppDispatch();
    const { isAuthenticated } = useAppSelector((state) => state.auth);
    const { items: wishlistItems } = useAppSelector((state) => state.wishlist);

    const isInWishlist = wishlistItems.some(item => item.fruitId === product.id);
    const isInStock = product.stockQuantity > 0;

    const handleAddToCart = () => {
        if (isAuthenticated && isInStock) {
            dispatch(addToCart({ fruitId: product.id, quantity: 1 }));
        }
    };

    const handleToggleWishlist = () => {
        if (!isAuthenticated) return;

        if (isInWishlist) {
            const wishlistItem = wishlistItems.find(item => item.fruitId === product.id);
            if (wishlistItem) {
                dispatch(removeFromWishlist(wishlistItem.id));
            }
        } else {
            dispatch(addToWishlist(product.id));
        }
    };

    return (
        <Card>
            <ProductImage>
                <img src={product.imageUrl || '/images/placeholder-fruit.jpg'} alt={product.fruitName} />
                <WishlistButton
                    onClick={handleToggleWishlist}
                    color={isInWishlist ? '#e91e63' : '#999'}
                    disabled={!isAuthenticated}
                >
                    ♥
                </WishlistButton>
            </ProductImage>

            <ProductInfo>
                <ProductName to={PATHS.PRODUCT_DETAIL(product.id)}>{product.fruitName}</ProductName>
                <ProductOrigin>{product.origin}</ProductOrigin>
                <ProductPrice>{formatPrice(product.price)}</ProductPrice>

                <ProductActions>
                    <AddToCartButton
                        onClick={handleAddToCart}
                        disabled={!isAuthenticated || !isInStock}
                    >
                        장바구니 담기
                    </AddToCartButton>

                    <StockStatus color={isInStock ? '#4caf50' : '#f44336'}>
                        {isInStock ? '재고 있음' : '품절'}
                    </StockStatus>
                </ProductActions>
            </ProductInfo>
        </Card>
    );
};

export default ProductCard;