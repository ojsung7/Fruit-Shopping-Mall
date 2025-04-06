// src/pages/Order/OrderCompletePage/index.tsx
import { useEffect, useState } from 'react';
import { Link, useSearchParams, useNavigate } from 'react-router-dom';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from '@/routes/paths';
import { formatPrice, formatDate } from '@/utils/formatters';
import styled from 'styled-components';
import API from '@/services/api';

interface OrderDetail {
    id: number;
    orderId: number;
    fruitId: number;
    fruitName: string;
    fruitImageUrl: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
}

interface Order {
    id: number;
    memberId: number;
    memberName: string;
    orderDate: string;
    totalPrice: number;
    paymentMethod: string;
    orderStatus: string;
    orderDetails: OrderDetail[];
}

const Container = styled.div`
  max-width: 800px;
  margin: 40px auto;
`;

const CompleteBanner = styled.div`
  text-align: center;
  padding: 40px;
  background-color: #e8f5e9;
  border-radius: 8px;
  margin-bottom: 40px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 16px;
  color: #2e7d32;
`;

const Subtitle = styled.p`
  font-size: 16px;
  color: #333;
  margin-bottom: 24px;
`;

const OrderInfoSection = styled.div`
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
`;

const SectionTitle = styled.h2`
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
`;

const OrderInfoGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  
  @media (max-width: 576px) {
    grid-template-columns: 1fr;
  }
`;

const InfoGroup = styled.div`
  margin-bottom: 16px;
`;

const InfoLabel = styled.div`
  font-weight: 600;
  margin-bottom: 4px;
  color: #666;
`;

const InfoValue = styled.div`
  color: #333;
`;

const OrderItemList = styled.div`
  margin-top: 16px;
`;

const OrderItem = styled.div`
  display: flex;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
  
  &:last-child {
    border-bottom: none;
  }
`;

const ItemImage = styled.div`
  width: 60px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ItemDetails = styled.div`
  flex: 1;
`;

const ItemName = styled.div`
  font-weight: 600;
  margin-bottom: 4px;
`;

const ItemInfo = styled.div`
  font-size: 14px;
  color: #666;
`;

const TotalRow = styled.div`
  display: flex;
  justify-content: space-between;
  font-weight: 600;
  font-size: 18px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #eee;
  color: #4caf50;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 16px;
  margin-top: 32px;
  
  @media (max-width: 576px) {
    flex-direction: column;
  }
`;

const PrimaryButton = styled(Link)`
  flex: 1;
  padding: 12px 24px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: 600;
  text-align: center;
  
  &:hover {
    background-color: #388e3c;
  }
`;

const SecondaryButton = styled(Link)`
  flex: 1;
  padding: 12px 24px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-weight: 600;
  text-align: center;
  
  &:hover {
    background-color: #eee;
  }
`;

const LoadingContainer = styled.div`
  text-align: center;
  padding: 40px;
`;

const ErrorContainer = styled.div`
  text-align: center;
  padding: 40px;
  color: #f44336;
`;

const OrderCompletePage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const orderId = searchParams.get('orderId');

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        if (!orderId) {
            navigate(PATHS.HOME);
            return;
        }

        const fetchOrderDetails = async () => {
            try {
                setLoading(true);
                const response = await API.get(`/orders/${orderId}`);
                setOrder(response.data);
            } catch (error) {
                console.error('주문 정보를 불러오는데 실패했습니다:', error);
                setError('주문 정보를 불러오는데 실패했습니다. 다시 시도해주세요.');
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [orderId, navigate, isAuthenticated]);

    if (loading) {
        return <LoadingContainer>주문 정보를 불러오는 중...</LoadingContainer>;
    }

    if (error) {
        return <ErrorContainer>{error}</ErrorContainer>;
    }

    if (!order) {
        return <ErrorContainer>주문 정보를 찾을 수 없습니다.</ErrorContainer>;
    }

    // 결제 방법 한글화
    const getPaymentMethodText = (method: string) => {
        switch (method) {
            case 'CARD':
                return '신용카드';
            case 'BANK_TRANSFER':
                return '계좌이체';
            case 'VIRTUAL_ACCOUNT':
                return '가상계좌';
            default:
                return method;
        }
    };

    // 주문 상태 한글화
    const getOrderStatusText = (status: string) => {
        switch (status) {
            case 'PENDING':
                return '결제 대기';
            case 'PAID':
                return '결제 완료';
            case 'PREPARING':
                return '상품 준비중';
            case 'SHIPPED':
                return '배송중';
            case 'DELIVERED':
                return '배송 완료';
            case 'CANCELLED':
                return '주문 취소';
            default:
                return status;
        }
    };

    return (
        <Container>
            <CompleteBanner>
                <PageTitle>주문이 완료되었습니다!</PageTitle>
                <Subtitle>
                    주문번호: <strong>{order.id}</strong>
                </Subtitle>
            </CompleteBanner>

            <OrderInfoSection>
                <SectionTitle>주문 정보</SectionTitle>
                <OrderInfoGrid>
                    <div>
                        <InfoGroup>
                            <InfoLabel>주문 일시</InfoLabel>
                            <InfoValue>{formatDate(order.orderDate)}</InfoValue>
                        </InfoGroup>
                        <InfoGroup>
                            <InfoLabel>주문자</InfoLabel>
                            <InfoValue>{order.memberName}</InfoValue>
                        </InfoGroup>
                        <InfoGroup>
                            <InfoLabel>주문 상태</InfoLabel>
                            <InfoValue>{getOrderStatusText(order.orderStatus)}</InfoValue>
                        </InfoGroup>
                    </div>
                    <div>
                        <InfoGroup>
                            <InfoLabel>결제 방법</InfoLabel>
                            <InfoValue>{getPaymentMethodText(order.paymentMethod)}</InfoValue>
                        </InfoGroup>
                        <InfoGroup>
                            <InfoLabel>결제 금액</InfoLabel>
                            <InfoValue>{formatPrice(order.totalPrice)}</InfoValue>
                        </InfoGroup>
                    </div>
                </OrderInfoGrid>
            </OrderInfoSection>

            <OrderInfoSection>
                <SectionTitle>주문 상품</SectionTitle>
                <OrderItemList>
                    {order.orderDetails.map((item) => (
                        <OrderItem key={item.id}>
                            <ItemImage>
                                <img
                                    src={item.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                    alt={item.fruitName}
                                />
                            </ItemImage>
                            <ItemDetails>
                                <ItemName>{item.fruitName}</ItemName>
                                <ItemInfo>{formatPrice(item.unitPrice)} × {item.quantity}개</ItemInfo>
                            </ItemDetails>
                            <div style={{ textAlign: 'right', fontWeight: 600 }}>
                                {formatPrice(item.totalPrice)}
                            </div>
                        </OrderItem>
                    ))}
                </OrderItemList>
                <TotalRow>
                    <span>총 결제 금액</span>
                    <span>{formatPrice(order.totalPrice)}</span>
                </TotalRow>
            </OrderInfoSection>

            <ActionButtons>
                <PrimaryButton to={PATHS.MY_ORDERS}>
                    주문 내역 확인
                </PrimaryButton>
                <SecondaryButton to={PATHS.HOME}>
                    쇼핑 계속하기
                </SecondaryButton>
            </ActionButtons>
        </Container>
    );
};

export default OrderCompletePage;