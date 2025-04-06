// src/pages/Cart/index.tsx
import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import {
    fetchCart,
    updateCartItem,
    removeFromCart,
    clearCart
} from '@/store/slices/cartSlice';
import { formatPrice } from '@/utils/formatters';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

const CartContainer = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const EmptyCart = styled.div`
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

const CartItemList = styled.div`
  margin-bottom: 24px;
`;

const CartItem = styled.div`
  display: grid;
  grid-template-columns: 100px 1fr auto;
  gap: 16px;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
  
  @media (max-width: 576px) {
    grid-template-columns: 80px 1fr;
  }
`;

const ItemImage = styled.div`
  width: 100px;
  height: 100px;
  overflow: hidden;
  border-radius: 8px;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  @media (max-width: 576px) {
    width: 80px;
    height: 80px;
  }
`;

const ItemInfo = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const ItemName = styled(Link)`
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  
  &:hover {
    color: #4caf50;
  }
`;

const ItemPrice = styled.div`
  font-size: 16px;
  font-weight: 600;
  color: #4caf50;
`;

const ItemActions = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
  
  @media (max-width: 576px) {
    grid-column: span 2;
    flex-direction: row;
    align-items: center;
    margin-top: 16px;
  }
`;

const QuantitySelector = styled.div`
  display: flex;
  align-items: center;
`;

const QuantityButton = styled.button`
  width: 28px;
  height: 28px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  
  &:hover {
    background-color: #eee;
  }
`;

const QuantityInput = styled.input`
  width: 40px;
  height: 28px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 0 4px;
  text-align: center;
  font-size: 14px;
`;

const RemoveButton = styled.button`
  background-color: transparent;
  border: none;
  color: #f44336;
  font-size: 14px;
  cursor: pointer;
  padding: 4px;
  
  &:hover {
    text-decoration: underline;
  }
`;

const CartSummary = styled.div`
  background-color: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const SummaryTitle = styled.h2`
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
`;

const SummaryRow = styled.div<{ isBold?: boolean }>`
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-weight: ${props => props.isBold ? '600' : '400'};
  
  &:last-child {
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #eee;
    font-size: 18px;
    color: #4caf50;
  }
`;

const CheckoutButton = styled(Link)`
  display: block;
  background-color: #4caf50;
  color: white;
  text-align: center;
  padding: 12px;
  border-radius: 4px;
  font-weight: 600;
  margin-top: 16px;
  
  &:hover {
    background-color: #388e3c;
  }
`;

const ClearCartButton = styled.button`
  display: block;
  background-color: transparent;
  color: #f44336;
  text-align: center;
  padding: 12px;
  border: 1px solid #f44336;
  border-radius: 4px;
  font-weight: 600;
  margin-top: 8px;
  cursor: pointer;
  
  &:hover {
    background-color: #ffebee;
  }
`;

const ActionButtons = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-top: 24px;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const ContinueShoppingButton = styled(Link)`
  padding: 12px;
  background-color: #f5f5f5;
  color: #333;
  text-align: center;
  border-radius: 4px;
  font-weight: 600;
  border: 1px solid #ddd;
  
  &:hover {
    background-color: #eee;
  }
`;

const CartPage = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const { items, totalItems, totalPrice, loading } = useAppSelector((state) => state.cart);
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        dispatch(fetchCart());
    }, [dispatch, isAuthenticated, navigate]);

    const handleQuantityChange = (id: number, quantity: number) => {
        if (quantity > 0) {
            dispatch(updateCartItem({ id, quantity }));
        }
    };

    const handleRemoveItem = (id: number) => {
        dispatch(removeFromCart(id));
    };

    const handleClearCart = () => {
        if (window.confirm('장바구니를 비우시겠습니까?')) {
            dispatch(clearCart());
        }
    };

    if (loading) {
        return <div>장바구니를 불러오는 중...</div>;
    }

    if (items.length === 0) {
        return (
            <CartContainer>
                <PageTitle>장바구니</PageTitle>
                <EmptyCart>
                    <h2>장바구니가 비어있습니다</h2>
                    <ContinueShoppingButton to={PATHS.PRODUCTS}>쇼핑 계속하기</ContinueShoppingButton>
                </EmptyCart>
            </CartContainer>
        );
    }

    // 배송비 (10만원 이상 무료배송)
    const shippingFee = totalPrice >= 100000 ? 0 : 3000;
    const finalTotal = totalPrice + shippingFee;

    return (
        <CartContainer>
            <PageTitle>장바구니</PageTitle>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: '24px' }}>
                <CartItemList>
                    {items.map((item) => (
                        <CartItem key={item.id}>
                            <ItemImage>
                                <img
                                    src={item.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                    alt={item.fruitName}
                                />
                            </ItemImage>

                            <ItemInfo>
                                <div>
                                    <ItemName to={PATHS.PRODUCT_DETAIL(item.fruitId)}>
                                        {item.fruitName}
                                    </ItemName>
                                </div>
                                <ItemPrice>{formatPrice(item.fruitPrice)}</ItemPrice>
                            </ItemInfo>

                            <ItemActions>
                                <QuantitySelector>
                                    <QuantityButton
                                        onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                                    >
                                        -
                                    </QuantityButton>
                                    <QuantityInput
                                        type="number"
                                        value={item.quantity}
                                        onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                        min={1}
                                    />
                                    <QuantityButton
                                        onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                                    >
                                        +
                                    </QuantityButton>
                                </QuantitySelector>

                                <div>
                                    <div style={{ fontWeight: 600, marginBottom: '4px', textAlign: 'right' }}>
                                        {formatPrice(item.totalPrice)}
                                    </div>
                                    <RemoveButton onClick={() => handleRemoveItem(item.id)}>
                                        삭제
                                    </RemoveButton>
                                </div>
                            </ItemActions>
                        </CartItem>
                    ))}
                </CartItemList>

                <div>
                    <CartSummary>
                        <SummaryTitle>주문 요약</SummaryTitle>
                        <SummaryRow>
                            <span>상품 수량</span>
                            <span>{totalItems}개</span>
                        </SummaryRow>
                        <SummaryRow>
                            <span>상품 금액</span>
                            <span>{formatPrice(totalPrice)}</span>
                        </SummaryRow>
                        <SummaryRow>
                            <span>배송비</span>
                            <span>{formatPrice(shippingFee)}</span>
                        </SummaryRow>
                        <SummaryRow isBold>
                            <span>총 결제 금액</span>
                            <span>{formatPrice(finalTotal)}</span>
                        </SummaryRow>

                        <CheckoutButton to={PATHS.CHECKOUT}>
                            주문하기
                        </CheckoutButton>

                        <ClearCartButton onClick={handleClearCart}>
                            장바구니 비우기
                        </ClearCartButton>
                    </CartSummary>

                    <ActionButtons>
                        <ContinueShoppingButton to={PATHS.PRODUCTS}>
                            쇼핑 계속하기
                        </ContinueShoppingButton>
                    </ActionButtons>
                </div>
            </div>
        </CartContainer>
    );
};

export default CartPage;