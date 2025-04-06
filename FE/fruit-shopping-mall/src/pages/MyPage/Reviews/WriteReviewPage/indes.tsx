// src/pages/Review/WriteReviewPage/index.tsx
import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';
import API from '@/services/api';

interface ReviewFormData {
    orderDetailId: number;
    rating: number;
    content: string;
    imageUrl?: string;
}

interface OrderDetail {
    id: number;
    orderId: number;
    fruitId: number;
    fruitName: string;
    fruitImageUrl: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
    hasReview?: boolean; // 옵셔널 필드로 추가
}

interface Order {
    id: number;
    orderStatus: string;
    orderDetails: OrderDetail[];
}

const Container = styled.div`
  max-width: 800px;
  margin: 40px auto;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const ReviewForm = styled.form`
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
`;

const SectionTitle = styled.h2`
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
`;

const ProductSelection = styled.div`
  margin-bottom: 24px;
`;

const ProductLabel = styled.label`
  display: block;
  font-weight: 600;
  margin-bottom: 12px;
`;

const ProductList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
`;

const ProductOption = styled.div<{ isSelected: boolean }>`
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  border: 1px solid ${props => props.isSelected ? '#4caf50' : '#ddd'};
  border-radius: 4px;
  cursor: pointer;
  background-color: ${props => props.isSelected ? '#e8f5e9' : 'white'};
  
  &:hover {
    border-color: #4caf50;
  }
`;

const ProductImage = styled.div`
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

const ProductInfo = styled.div`
  flex: 1;
`;

const ProductName = styled.div`
  font-weight: 600;
  margin-bottom: 4px;
`;

const ProductDetails = styled.div`
  font-size: 14px;
  color: #666;
`;

const RatingSection = styled.div`
  margin-bottom: 24px;
`;

const RatingLabel = styled.label`
  display: block;
  font-weight: 600;
  margin-bottom: 12px;
`;

const RatingStars = styled.div`
  display: flex;
  gap: 8px;
`;

const StarButton = styled.button<{ filled: boolean }>`
  background-color: transparent;
  border: none;
  font-size: 30px;
  color: ${props => props.filled ? '#FFD700' : '#ccc'};
  cursor: pointer;
  transition: transform 0.1s;
  
  &:hover {
    transform: scale(1.1);
  }
`;

const TextSection = styled.div`
  margin-bottom: 24px;
`;

const TextLabel = styled.label`
  display: block;
  font-weight: 600;
  margin-bottom: 12px;
`;

const TextArea = styled.textarea`
  width: 100%;
  min-height: 150px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  resize: vertical;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const CharCount = styled.div`
  text-align: right;
  font-size: 14px;
  color: #666;
  margin-top: 4px;
`;

const ImageSection = styled.div`
  margin-bottom: 24px;
`;

const ImageLabel = styled.label`
  display: block;
  font-weight: 600;
  margin-bottom: 12px;
`;

const ImageInput = styled.div`
  padding: 12px;
  border: 1px dashed #ddd;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  
  &:hover {
    background-color: #f5f5f5;
  }
`;

const ImagePreview = styled.div`
  margin-top: 16px;
  
  img {
    max-width: 100%;
    max-height: 300px;
    border-radius: 4px;
  }
`;

const SubmitButton = styled.button`
  padding: 12px 24px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const ErrorMessage = styled.p`
  color: #f44336;
  font-size: 14px;
  margin-top: 8px;
`;

const CancelButton = styled.button`
  padding: 12px 24px;
  background-color: white;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  margin-right: 12px;
  
  &:hover {
    background-color: #f5f5f5;
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const LoadingContainer = styled.div`
  text-align: center;
  padding: 40px;
`;

const WriteReviewPage = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<number | null>(null);
    const [rating, setRating] = useState(0);
    const [imagePreview, setImagePreview] = useState<string | null>(null);
    const [orderDetailsWithReview, setOrderDetailsWithReview] = useState<(OrderDetail & { hasReview: boolean })[]>([]);

    const { register, handleSubmit, watch, formState: { errors } } = useForm<ReviewFormData>();

    const orderId = searchParams.get('orderId');
    const content = watch('content', '');

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        if (!orderId) {
            navigate(PATHS.MY_ORDERS);
            return;
        }

        const fetchOrderDetails = async () => {
            try {
                setLoading(true);
                const response = await API.get(`/orders/${orderId}`);
                setOrder(response.data);

                // 각 주문 상세에 대해 리뷰 작성 여부 확인
                const orderDetails = response.data.orderDetails;
                const detailsWithReviewStatus = await Promise.all(
                    orderDetails.map(async (detail: OrderDetail) => {
                        try {
                            // 해당 주문 상세에 대한 리뷰 존재 여부 확인
                            await API.get(`/reviews/order-detail/${detail.id}`);
                            // 에러가 발생하지 않으면 리뷰가 있는 것
                            return { ...detail, hasReview: true };
                        } catch (error) {
                            // 리뷰가 없으면
                            return { ...detail, hasReview: false };
                        }
                    })
                );

                setOrderDetailsWithReview(detailsWithReviewStatus);

                // 리뷰가 없는 첫 번째 상품을 선택
                const firstReviewableItem = detailsWithReviewStatus.find(detail => !detail.hasReview);
                if (firstReviewableItem) {
                    setSelectedProduct(firstReviewableItem.id);
                }
            } catch (error) {
                console.error('주문 정보를 불러오는데 실패했습니다:', error);
                alert('주문 정보를 불러오는데 실패했습니다.');
                navigate(PATHS.MY_ORDERS);
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [orderId, navigate, isAuthenticated]);

    // 나머지 핸들러 함수들은 동일하게 유지

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (file.size > 5 * 1024 * 1024) {
            alert('이미지 크기는 5MB를 초과할 수 없습니다.');
            return;
        }

        // 이미지 미리보기
        const reader = new FileReader();
        reader.onload = () => {
            setImagePreview(reader.result as string);
        };
        reader.readAsDataURL(file);

        // 실제 업로드 로직은 여기에 추가
        // 현재는 단순히 미리보기만 구현
    };

    const onSubmit = async (data: ReviewFormData) => {
        if (!selectedProduct) {
            alert('리뷰를 작성할 상품을 선택해주세요.');
            return;
        }

        if (rating === 0) {
            alert('별점을 선택해주세요.');
            return;
        }

        try {
            setSubmitting(true);

            // 리뷰 데이터 준비
            const reviewData = {
                orderDetailId: selectedProduct,
                rating,
                content: data.content,
                imageUrl: imagePreview || undefined // 실제로는 이미지 업로드 후 URL을 받아서 사용
            };

            // 리뷰 등록 API 호출
            await API.post('/reviews', reviewData);

            alert('리뷰가 등록되었습니다.');
            navigate(PATHS.MY_REVIEWS);
        } catch (error) {
            console.error('리뷰 등록에 실패했습니다:', error);
            alert('리뷰 등록에 실패했습니다. 다시 시도해주세요.');
        } finally {
            setSubmitting(false);
        }
    };

    const handleCancel = () => {
        if (window.confirm('작성 중인 내용이 저장되지 않습니다. 취소하시겠습니까?')) {
            navigate(PATHS.MY_ORDERS);
        }
    };

    if (loading) {
        return <LoadingContainer>주문 정보를 불러오는 중...</LoadingContainer>;
    }

    if (!order) {
        return <LoadingContainer>주문 정보를 찾을 수 없습니다.</LoadingContainer>;
    }

    // 배송 완료 상태인지 확인
    if (order.orderStatus !== 'DELIVERED') {
        return (
            <Container>
                <PageTitle>리뷰 작성</PageTitle>
                <div style={{ textAlign: 'center', padding: '40px' }}>
                    <h2 style={{ marginBottom: '16px' }}>배송 완료된 상품만 리뷰를 작성할 수 있습니다.</h2>
                    <CancelButton onClick={() => navigate(PATHS.MY_ORDERS)}>
                        주문 내역으로 돌아가기
                    </CancelButton>
                </div>
            </Container>
        );
    }

    // 리뷰 작성 가능한 상품이 있는지 확인
    const reviewableProducts = orderDetailsWithReview.filter(detail => !detail.hasReview);

    if (reviewableProducts.length === 0) {
        return (
            <Container>
                <PageTitle>리뷰 작성</PageTitle>
                <div style={{ textAlign: 'center', padding: '40px' }}>
                    <h2 style={{ marginBottom: '16px' }}>이 주문의 모든 상품에 대해 이미 리뷰를 작성했습니다.</h2>
                    <CancelButton onClick={() => navigate(PATHS.MY_ORDERS)}>
                        주문 내역으로 돌아가기
                    </CancelButton>
                </div>
            </Container>
        );
    }

    return (
        <Container>
            <PageTitle>리뷰 작성</PageTitle>

            <ReviewForm onSubmit={handleSubmit(onSubmit)}>
                <SectionTitle>상품 선택</SectionTitle>
                <ProductSelection>
                    <ProductLabel>리뷰를 작성할 상품을 선택해주세요</ProductLabel>
                    <ProductList>
                        {/* 리뷰가 없는 상품만 표시 */}
                        {orderDetailsWithReview.filter(detail => !detail.hasReview).map((detail) => (
                            <ProductOption
                                key={detail.id}
                                isSelected={selectedProduct === detail.id}
                                onClick={() => setSelectedProduct(detail.id)}
                            >
                                <ProductImage>
                                    <img
                                        src={detail.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                        alt={detail.fruitName}
                                    />
                                </ProductImage>
                                <ProductInfo>
                                    <ProductName>{detail.fruitName}</ProductName>
                                    <ProductDetails>
                                        {detail.quantity}개 구매
                                    </ProductDetails>
                                </ProductInfo>
                            </ProductOption>
                        ))}
                    </ProductList>
                    {!selectedProduct && (
                        <ErrorMessage>상품을 선택해주세요</ErrorMessage>
                    )}
                </ProductSelection>

                {/* 나머지 폼 부분은 동일하게 유지 */}
                <SectionTitle>별점</SectionTitle>
                <RatingSection>
                    <RatingLabel>이 상품에 대한 만족도를 선택해주세요</RatingLabel>
                    <RatingStars>
                        {[1, 2, 3, 4, 5].map((star) => (
                            <StarButton
                                key={star}
                                type="button"
                                filled={star <= rating}
                                onClick={() => setRating(star)}
                            >
                                ★
                            </StarButton>
                        ))}
                    </RatingStars>
                    {rating === 0 && (
                        <ErrorMessage>별점을 선택해주세요</ErrorMessage>
                    )}
                </RatingSection>

                <SectionTitle>리뷰 내용</SectionTitle>
                <TextSection>
                    <TextLabel htmlFor="content">상품에 대한 솔직한 리뷰를 작성해주세요</TextLabel>
                    <TextArea
                        id="content"
                        {...register('content', {
                            required: '리뷰 내용을 입력해주세요',
                            minLength: {
                                value: 10,
                                message: '최소 10자 이상 입력해주세요'
                            },
                            maxLength: {
                                value: 500,
                                message: '최대 500자까지 입력 가능합니다'
                            }
                        })}
                        placeholder="상품의 맛, 신선도, 배송 상태 등 구매 경험을 자세히 알려주세요."
                    />
                    <CharCount>{content.length} / 500자</CharCount>
                    {errors.content && (
                        <ErrorMessage>{errors.content.message}</ErrorMessage>
                    )}
                </TextSection>

                <SectionTitle>사진 첨부 (선택)</SectionTitle>
                <ImageSection>
                    <ImageLabel>상품 사진을 첨부해주세요 (최대 5MB)</ImageLabel>
                    <ImageInput onClick={() => document.getElementById('image-upload')?.click()}>
                        <input
                            id="image-upload"
                            type="file"
                            accept="image/*"
                            style={{ display: 'none' }}
                            onChange={handleImageChange}
                        />
                        <p>클릭하여 이미지 업로드</p>
                    </ImageInput>
                    {imagePreview && (
                        <ImagePreview>
                            <img src={imagePreview} alt="미리보기" />
                        </ImagePreview>
                    )}
                </ImageSection>

                <ButtonGroup>
                    <CancelButton type="button" onClick={handleCancel}>
                        취소
                    </CancelButton>
                    <SubmitButton type="submit" disabled={submitting}>
                        {submitting ? '등록 중...' : '리뷰 등록'}
                    </SubmitButton>
                </ButtonGroup>
            </ReviewForm>
        </Container>
    );
};

export default WriteReviewPage;