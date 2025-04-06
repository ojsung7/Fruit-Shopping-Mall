// src/pages/Product/ProductDetailPage/index.tsx
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchProductById } from '@/store/slices/productSlice';
import { addToCart } from '@/store/slices/cartSlice';
import { addToWishlist, removeFromWishlist } from '@/store/slices/wishlistSlice';
import { formatPrice } from '@/utils/formatters';
import styled from 'styled-components';

const Container = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  margin-top: 20px;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const ProductImage = styled.div`
  border-radius: 8px;
  overflow: hidden;
  
  img {
    width: 100%;
    height: auto;
    object-fit: cover;
  }
`;

const ProductInfo = styled.div`
  display: flex;
  flex-direction: column;
`;

const ProductName = styled.h1`
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
`;

const ProductOrigin = styled.p`
  font-size: 16px;
  color: #666;
  margin-bottom: 16px;
`;

const ProductPrice = styled.div`
  font-size: 24px;
  font-weight: 700;
  color: #4caf50;
  margin-bottom: 24px;
`;

const ProductDescription = styled.div`
  margin-bottom: 24px;
  line-height: 1.6;
  color: #333;
`;

const Label = styled.div`
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
`;

const InfoTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 24px;
  
  td {
    padding: 8px;
    border-bottom: 1px solid #eee;
  }
  
  td:first-child {
    width: 100px;
    font-weight: 600;
    color: #666;
  }
`;

const QuantitySelector = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 24px;
`;

const QuantityButton = styled.button`
  width: 36px;
  height: 36px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  cursor: pointer;
  
  &:hover {
    background-color: #eee;
  }
  
  &:disabled {
    background-color: #f5f5f5;
    color: #ccc;
    cursor: not-allowed;
  }
`;

const QuantityInput = styled.input`
  width: 60px;
  height: 36px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 0 8px;
  text-align: center;
  font-size: 16px;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
`;

const AddToCartButton = styled.button`
  flex: 1;
  padding: 12px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const WishlistButton = styled.button<{ isActive: boolean }>`
  padding: 12px 16px;
  background-color: ${props => props.isActive ? '#ffebee' : '#f5f5f5'};
  color: ${props => props.isActive ? '#e91e63' : '#333'};
  border: 1px solid ${props => props.isActive ? '#e91e63' : '#ddd'};
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    background-color: ${props => props.isActive ? '#fce4ec' : '#eee'};
  }
`;

const StockInfo = styled.div<{ inStock: boolean }>`
  padding: 12px 16px;
  background-color: ${props => props.inStock ? '#e8f5e9' : '#ffebee'};
  color: ${props => props.inStock ? '#4caf50' : '#f44336'};
  border-radius: 4px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 24px;
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
  width: 100%;
`;

const ErrorContainer = styled.div`
  text-align: center;
  padding: 40px;
  color: #f44336;
`;

const ProductDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useAppDispatch();
  const { selectedProduct, loading, error } = useAppSelector((state) => state.product);
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const { items: wishlistItems } = useAppSelector((state) => state.wishlist);

  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    if (id) {
      dispatch(fetchProductById(parseInt(id)));
    }
  }, [dispatch, id]);

  const isInWishlist = wishlistItems.some(item => item.fruitId === selectedProduct?.id);
  const isInStock = selectedProduct ? selectedProduct.stockQuantity > 0 : false;

  const handleAddToCart = () => {
    if (isAuthenticated && selectedProduct && isInStock) {
      dispatch(addToCart({ fruitId: selectedProduct.id, quantity }));
    }
  };

  const handleToggleWishlist = () => {
    if (!isAuthenticated || !selectedProduct) return;

    if (isInWishlist) {
      const wishlistItem = wishlistItems.find(item => item.fruitId === selectedProduct.id);
      if (wishlistItem) {
        dispatch(removeFromWishlist(wishlistItem.id));
      }
    } else {
      dispatch(addToWishlist(selectedProduct.id));
    }
  };

  const incrementQuantity = () => {
    if (selectedProduct && quantity < selectedProduct.stockQuantity) {
      setQuantity(quantity + 1);
    }
  };

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  if (loading) {
    return <LoadingContainer>상품 정보를 불러오는 중...</LoadingContainer>;
  }

  if (error) {
    return <ErrorContainer>상품 정보를 불러오는데 실패했습니다: {error}</ErrorContainer>;
  }

  if (!selectedProduct) {
    return <ErrorContainer>상품 정보를 찾을 수 없습니다.</ErrorContainer>;
  }

  return (
    <div>
      <Container>
        <ProductImage>
          <img
            src={selectedProduct.imageUrl || '/images/placeholder-fruit.jpg'}
            alt={selectedProduct.fruitName}
          />
        </ProductImage>

        <ProductInfo>
          <ProductName>{selectedProduct.fruitName}</ProductName>
          <ProductOrigin>{selectedProduct.origin}</ProductOrigin>
          <ProductPrice>{formatPrice(selectedProduct.price)}</ProductPrice>

          <StockInfo inStock={isInStock}>
            {isInStock
              ? `재고 ${selectedProduct.stockQuantity}개 남음`
              : '품절된 상품입니다'
            }
          </StockInfo>

          <ProductDescription>
            <Label>상품 설명</Label>
            <p>{selectedProduct.description || '상세 설명이 없습니다.'}</p>
          </ProductDescription>

          <InfoTable>
            <tbody>
              <tr>
                <td>원산지</td>
                <td>{selectedProduct.origin}</td>
              </tr>
              <tr>
                <td>제철</td>
                <td>{selectedProduct.season || '연중'}</td>
              </tr>
              <tr>
                <td>카테고리</td>
                <td>{selectedProduct.categoryName}</td>
              </tr>
            </tbody>
          </InfoTable>

          <QuantitySelector>
            <QuantityButton
              onClick={decrementQuantity}
              disabled={quantity <= 1 || !isInStock}
            >
              -
            </QuantityButton>
            <QuantityInput
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value))}
              min={1}
              max={selectedProduct.stockQuantity}
              disabled={!isInStock}
            />
            <QuantityButton
              onClick={incrementQuantity}
              disabled={quantity >= selectedProduct.stockQuantity || !isInStock}
            >
              +
            </QuantityButton>
          </QuantitySelector>

          <ActionButtons>
            <AddToCartButton
              onClick={handleAddToCart}
              disabled={!isAuthenticated || !isInStock}
            >
              장바구니 담기
            </AddToCartButton>

            <WishlistButton
              onClick={handleToggleWishlist}
              isActive={isInWishlist}
              disabled={!isAuthenticated}
            >
              ♥
            </WishlistButton>
          </ActionButtons>
        </ProductInfo>
      </Container>
    </div>
  );
};

export default ProductDetailPage;