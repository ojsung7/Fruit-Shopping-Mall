// src/pages/Wishlist/index.tsx
import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchWishlist, removeFromWishlist, clearWishlist } from '@/store/slices/wishlistSlice';
import { addToCart } from '@/store/slices/cartSlice';
import { formatPrice } from '@/utils/formatters';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

const WishlistContainer = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const EmptyWishlist = styled.div`
  text-align: center;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  h2 {
    font-size: 20px;
    margin-bottom: 16px;
    color: #666;
  }
`;

const WishlistGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 24px;
`;

const WishlistItem = styled.div`
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-5px);
  }
`;

const ItemImage = styled.div`
  height: 180px;
  overflow: hidden;
  position: relative;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const RemoveButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: white;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  color: #f44336;
  
  &:hover {
    background-color: #ffebee;
  }
`;

const ItemInfo = styled.div`
  padding: 16px;
`;

const ItemName = styled(Link)`
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  display: block;
  color: #333;
  
  &:hover {
    color: #4caf50;
  }
`;

const ItemOrigin = styled.div`
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
`;

const ItemPrice = styled.div`
  font-size: 18px;
  font-weight: 700;
  color: #4caf50;
  margin-bottom: 16px;
`;

const ItemActions = styled.div`
  display: flex;
`;

const AddToCartButton = styled.button`
  flex: 1;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 0;
  font-weight: 500;
  cursor: pointer;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const ActionButtons = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
`;

const ContinueShoppingButton = styled(Link)`
  padding: 12px 24px;
  background-color: #f5f5f5;
  color: #333;
  border-radius: 4px;
  font-weight: 600;
  border: 1px solid #ddd;
  
  &:hover {
    background-color: #eee;
  }
`;

const ClearWishlistButton = styled.button`
  padding: 12px 24px;
  background-color: transparent;
  color: #f44336;
  border: 1px solid #f44336;
  border-radius: 4px;
  font-weight: 600;
  cursor: pointer;
  
  &:hover {
    background-color: #ffebee;
  }
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  font-size: 18px;
  color: #666;
`;

const WishlistPage = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const { items, loading } = useAppSelector((state) => state.wishlist);
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        dispatch(fetchWishlist());
    }, [dispatch, isAuthenticated, navigate]);

    const handleRemoveItem = (id: number) => {
        dispatch(removeFromWishlist(id));
    };

    const handleAddToCart = (fruitId: number) => {
        dispatch(addToCart({ fruitId, quantity: 1 }));
    };

    const handleClearWishlist = () => {
        if (window.confirm('위시리스트를 비우시겠습니까?')) {
            dispatch(clearWishlist());
        }
    };

    if (loading) {
        return <LoadingContainer>위시리스트를 불러오는 중...</LoadingContainer>;
    }

    if (items.length === 0) {
        return (
            <WishlistContainer>
                <PageTitle>위시리스트</PageTitle>
                <EmptyWishlist>
                    <h2>위시리스트가 비어있습니다</h2>
                    <ContinueShoppingButton to={PATHS.PRODUCTS}>쇼핑 계속하기</ContinueShoppingButton>
                </EmptyWishlist>
            </WishlistContainer>
        );
    }

    return (
        <WishlistContainer>
            <PageTitle>위시리스트</PageTitle>

            <WishlistGrid>
                {items.map((item) => (
                    <WishlistItem key={item.id}>
                        <ItemImage>
                            <img
                                src={item.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                alt={item.fruitName}
                            />
                            <RemoveButton onClick={() => handleRemoveItem(item.id)}>
                                ×
                            </RemoveButton>
                        </ItemImage>

                        <ItemInfo>
                            <ItemName to={PATHS.PRODUCT_DETAIL(item.fruitId)}>
                                {item.fruitName}
                            </ItemName>
                            <ItemOrigin>{item.origin}</ItemOrigin>
                            <ItemPrice>{formatPrice(item.fruitPrice)}</ItemPrice>

                            <ItemActions>
                                <AddToCartButton
                                    onClick={() => handleAddToCart(item.fruitId)}
                                    disabled={item.stockQuantity <= 0}
                                >
                                    {item.stockQuantity > 0 ? '장바구니에 담기' : '품절'}
                                </AddToCartButton>
                            </ItemActions>
                        </ItemInfo>
                    </WishlistItem>
                ))}
            </WishlistGrid>

            <ActionButtons>
                <ContinueShoppingButton to={PATHS.PRODUCTS}>
                    쇼핑 계속하기
                </ContinueShoppingButton>

                <ClearWishlistButton onClick={handleClearWishlist}>
                    위시리스트 비우기
                </ClearWishlistButton>
            </ActionButtons>
        </WishlistContainer>
    );
};

export default WishlistPage;