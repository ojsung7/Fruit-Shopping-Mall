// src/pages/Order/CheckoutPage/index.tsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchCart } from '@/store/slices/cartSlice';
import { formatPrice } from '@/utils/formatters';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';
import API from '@/services/api';

interface DeliveryFormData {
    recipient: string;
    phoneNumber: string;
    zipCode: string;
    address1: string;
    address2: string;
    deliveryRequest: string;
}

interface PaymentFormData {
    paymentMethod: 'CARD' | 'BANK_TRANSFER' | 'VIRTUAL_ACCOUNT';
    cardNumber?: string;
    cardExpiry?: string;
    cardCVC?: string;
    bankAccount?: string;
    accountHolder?: string;
}

const CheckoutContainer = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const CheckoutGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 24px;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const Section = styled.div`
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
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
`;

const Input = styled.input`
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const TextArea = styled.textarea`
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-height: 80px;
  resize: vertical;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const Select = styled.select`
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const ErrorMessage = styled.p`
  color: #f44336;
  font-size: 14px;
  margin-top: 4px;
`;

const AddressRow = styled.div`
  display: flex;
  gap: 8px;
  
  @media (max-width: 576px) {
    flex-direction: column;
  }
`;

const ZipCodeInput = styled(Input)`
  width: 100px;
  @media (max-width: 576px) {
    width: 100%;
  }
`;

const SearchAddressButton = styled.button`
  padding: 10px 16px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
  
  &:hover {
    background-color: #eee;
  }
`;

const PaymentMethodGroup = styled.div`
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  
  @media (max-width: 576px) {
    flex-direction: column;
  }
`;

const PaymentMethodButton = styled.button<{ isSelected: boolean }>`
  flex: 1;
  padding: 12px;
  border: 1px solid ${props => props.isSelected ? '#4caf50' : '#ddd'};
  border-radius: 4px;
  background-color: ${props => props.isSelected ? '#e8f5e9' : 'white'};
  color: ${props => props.isSelected ? '#4caf50' : '#333'};
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    background-color: ${props => props.isSelected ? '#e8f5e9' : '#f5f5f5'};
  }
`;

const CartSummary = styled.div`
  margin-bottom: 16px;
`;

const CartItems = styled.div`
  margin-bottom: 16px;
  max-height: 300px;
  overflow-y: auto;
`;

const CartItem = styled.div`
  display: flex;
  gap: 12px;
  padding: 12px 0;
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
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ItemInfo = styled.div`
  flex: 1;
`;

const ItemName = styled.div`
  font-weight: 600;
  margin-bottom: 4px;
`;

const ItemPrice = styled.div`
  font-size: 14px;
  color: #666;
`;

const ItemQuantity = styled.div`
  font-size: 14px;
  color: #666;
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

const PlaceOrderButton = styled.button`
  width: 100%;
  padding: 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 24px;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const CheckoutPage = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const { items, totalItems, totalPrice, loading } = useAppSelector((state) => state.cart);
    const { isAuthenticated, user } = useAppSelector((state) => state.auth);

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [paymentMethod, setPaymentMethod] = useState<'CARD' | 'BANK_TRANSFER' | 'VIRTUAL_ACCOUNT'>('CARD');

    const {
        register: registerDelivery,
        handleSubmit: handleSubmitDelivery,
        formState: { errors: deliveryErrors },
        setValue: setDeliveryValue
    } = useForm<DeliveryFormData>();

    const {
        register: registerPayment,
        handleSubmit: handleSubmitPayment,
        formState: { errors: paymentErrors },
        watch: watchPayment
    } = useForm<PaymentFormData>({
        defaultValues: {
            paymentMethod: 'CARD'
        }
    });

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        dispatch(fetchCart());

        // 사용자 정보로 배송지 초기화
        if (user) {
            setDeliveryValue('recipient', user.name || '');
            setDeliveryValue('phoneNumber', user.phoneNumber || '');
            if (user.address) {
                // 주소 형식에 따라 파싱 로직 필요
                setDeliveryValue('address1', user.address);
            }
        }
    }, [dispatch, isAuthenticated, navigate, user, setDeliveryValue]);

    // 배송비 (10만원 이상 무료배송)
    const shippingFee = totalPrice >= 100000 ? 0 : 3000;
    const finalTotal = totalPrice + shippingFee;

    const handleSearchAddress = () => {
        // 주소 검색 API 연동 (예: 다음 우편번호 서비스)
        alert('주소 검색 기능은 추후 구현 예정입니다.');
    };

    const onSubmit = async (deliveryData: DeliveryFormData) => {
        // 폼 모두 제출
        const paymentData = watchPayment();

        try {
            setIsSubmitting(true);

            // 주문 생성 API 호출
            const orderItems = items.map(item => ({
                fruitId: item.fruitId,
                quantity: item.quantity
            }));

            const orderData = {
                orderItems,
                paymentMethod: paymentData.paymentMethod,
                deliveryInfo: {
                    ...deliveryData
                }
            };

            const response = await API.post('/orders', orderData);

            // 주문 완료 페이지로 이동
            navigate(`${PATHS.ORDER_COMPLETE}?orderId=${response.data.id}`);
        } catch (error) {
            console.error('주문 처리 중 오류가 발생했습니다:', error);
            alert('주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.');
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return <div>장바구니 정보를 불러오는 중...</div>;
    }

    if (items.length === 0) {
        return (
            <CheckoutContainer>
                <PageTitle>결제하기</PageTitle>
                <Section>
                    <p>장바구니가 비어있습니다. 상품을 먼저 장바구니에 담아주세요.</p>
                </Section>
            </CheckoutContainer>
        );
    }

    return (
        <CheckoutContainer>
            <PageTitle>결제하기</PageTitle>

            <Form onSubmit={handleSubmitDelivery(onSubmit)}>
                <CheckoutGrid>
                    <div>
                        <Section>
                            <SectionTitle>배송 정보</SectionTitle>

                            <FormGroup>
                                <Label htmlFor="recipient">받는 사람 *</Label>
                                <Input
                                    id="recipient"
                                    {...registerDelivery('recipient', {
                                        required: '받는 사람 이름을 입력해주세요'
                                    })}
                                />
                                {deliveryErrors.recipient && (
                                    <ErrorMessage>{deliveryErrors.recipient.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="phoneNumber">연락처 *</Label>
                                <Input
                                    id="phoneNumber"
                                    placeholder="010-0000-0000"
                                    {...registerDelivery('phoneNumber', {
                                        required: '연락처를 입력해주세요',
                                        pattern: {
                                            value: /^\d{3}-\d{3,4}-\d{4}$/,
                                            message: '올바른 연락처 형식이 아닙니다 (예: 010-1234-5678)'
                                        }
                                    })}
                                />
                                {deliveryErrors.phoneNumber && (
                                    <ErrorMessage>{deliveryErrors.phoneNumber.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="zipCode">우편번호 *</Label>
                                <AddressRow>
                                    <ZipCodeInput
                                        id="zipCode"
                                        {...registerDelivery('zipCode', {
                                            required: '우편번호를 입력해주세요'
                                        })}
                                    />
                                    <SearchAddressButton type="button" onClick={handleSearchAddress}>
                                        주소 검색
                                    </SearchAddressButton>
                                </AddressRow>
                                {deliveryErrors.zipCode && (
                                    <ErrorMessage>{deliveryErrors.zipCode.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="address1">주소 *</Label>
                                <Input
                                    id="address1"
                                    {...registerDelivery('address1', {
                                        required: '주소를 입력해주세요'
                                    })}
                                />
                                {deliveryErrors.address1 && (
                                    <ErrorMessage>{deliveryErrors.address1.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="address2">상세 주소</Label>
                                <Input
                                    id="address2"
                                    {...registerDelivery('address2')}
                                />
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="deliveryRequest">배송 요청사항</Label>
                                <TextArea
                                    id="deliveryRequest"
                                    placeholder="배송 시 요청사항을 입력해주세요"
                                    {...registerDelivery('deliveryRequest')}
                                />
                            </FormGroup>
                        </Section>

                        <Section>
                            <SectionTitle>결제 정보</SectionTitle>

                            <PaymentMethodGroup>
                                <PaymentMethodButton
                                    type="button"
                                    isSelected={paymentMethod === 'CARD'}
                                    onClick={() => setPaymentMethod('CARD')}
                                >
                                    신용카드
                                </PaymentMethodButton>
                                <PaymentMethodButton
                                    type="button"
                                    isSelected={paymentMethod === 'BANK_TRANSFER'}
                                    onClick={() => setPaymentMethod('BANK_TRANSFER')}
                                >
                                    계좌이체
                                </PaymentMethodButton>
                                <PaymentMethodButton
                                    type="button"
                                    isSelected={paymentMethod === 'VIRTUAL_ACCOUNT'}
                                    onClick={() => setPaymentMethod('VIRTUAL_ACCOUNT')}
                                >
                                    가상계좌
                                </PaymentMethodButton>
                            </PaymentMethodGroup>

                            <input
                                type="hidden"
                                {...registerPayment('paymentMethod', { required: true })}
                                value={paymentMethod}
                            />

                            {paymentMethod === 'CARD' && (
                                <>
                                    <FormGroup>
                                        <Label htmlFor="cardNumber">카드 번호 *</Label>
                                        <Input
                                            id="cardNumber"
                                            placeholder="0000-0000-0000-0000"
                                            {...registerPayment('cardNumber', {
                                                required: '카드 번호를 입력해주세요',
                                                pattern: {
                                                    value: /^\d{4}-\d{4}-\d{4}-\d{4}$/,
                                                    message: '올바른 카드 번호 형식이 아닙니다'
                                                }
                                            })}
                                        />
                                        {paymentErrors.cardNumber && (
                                            <ErrorMessage>{paymentErrors.cardNumber.message}</ErrorMessage>
                                        )}
                                    </FormGroup>

                                    <FormGroup>
                                        <Label htmlFor="cardExpiry">유효 기간 *</Label>
                                        <Input
                                            id="cardExpiry"
                                            placeholder="MM/YY"
                                            {...registerPayment('cardExpiry', {
                                                required: '유효 기간을 입력해주세요',
                                                pattern: {
                                                    value: /^(0[1-9]|1[0-2])\/\d{2}$/,
                                                    message: '올바른 유효 기간 형식이 아닙니다 (예: 01/26)'
                                                }
                                            })}
                                        />
                                        {paymentErrors.cardExpiry && (
                                            <ErrorMessage>{paymentErrors.cardExpiry.message}</ErrorMessage>
                                        )}
                                    </FormGroup>

                                    <FormGroup>
                                        <Label htmlFor="cardCVC">CVC *</Label>
                                        <Input
                                            id="cardCVC"
                                            placeholder="000"
                                            {...registerPayment('cardCVC', {
                                                required: 'CVC를 입력해주세요',
                                                pattern: {
                                                    value: /^\d{3}$/,
                                                    message: '올바른 CVC 형식이 아닙니다 (3자리 숫자)'
                                                }
                                            })}
                                        />
                                        {paymentErrors.cardCVC && (
                                            <ErrorMessage>{paymentErrors.cardCVC.message}</ErrorMessage>
                                        )}
                                    </FormGroup>
                                </>
                            )}

                            {paymentMethod === 'BANK_TRANSFER' && (
                                <>
                                    <FormGroup>
                                        <Label htmlFor="bankAccount">계좌번호 *</Label>
                                        <Input
                                            id="bankAccount"
                                            placeholder="000-000000-00-000"
                                            {...registerPayment('bankAccount', {
                                                required: '계좌번호를 입력해주세요'
                                            })}
                                        />
                                        {paymentErrors.bankAccount && (
                                            <ErrorMessage>{paymentErrors.bankAccount.message}</ErrorMessage>
                                        )}
                                    </FormGroup>

                                    <FormGroup>
                                        <Label htmlFor="accountHolder">예금주 *</Label>
                                        <Input
                                            id="accountHolder"
                                            {...registerPayment('accountHolder', {
                                                required: '예금주를 입력해주세요'
                                            })}
                                        />
                                        {paymentErrors.accountHolder && (
                                            <ErrorMessage>{paymentErrors.accountHolder.message}</ErrorMessage>
                                        )}
                                    </FormGroup>
                                </>
                            )}

                            {paymentMethod === 'VIRTUAL_ACCOUNT' && (
                                <p>주문 완료 후 가상계좌 정보가 발급됩니다.</p>
                            )}
                        </Section>
                    </div>

                    <div>
                        <Section>
                            <SectionTitle>주문 요약</SectionTitle>

                            <CartSummary>
                                <CartItems>
                                    {items.map((item) => (
                                        <CartItem key={item.id}>
                                            <ItemImage>
                                                <img
                                                    src={item.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                                    alt={item.fruitName}
                                                />
                                            </ItemImage>
                                            <ItemInfo>
                                                <ItemName>{item.fruitName}</ItemName>
                                                <ItemPrice>{formatPrice(item.fruitPrice)}</ItemPrice>
                                                <ItemQuantity>{item.quantity}개</ItemQuantity>
                                            </ItemInfo>
                                        </CartItem>
                                    ))}
                                </CartItems>

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
                            </CartSummary>

                            <PlaceOrderButton type="submit" disabled={isSubmitting}>
                                {isSubmitting ? '주문 처리 중...' : `${formatPrice(finalTotal)} 결제하기`}
                            </PlaceOrderButton>
                        </Section>
                    </div>
                </CheckoutGrid>
            </Form>
        </CheckoutContainer>
    );
};

export default CheckoutPage;