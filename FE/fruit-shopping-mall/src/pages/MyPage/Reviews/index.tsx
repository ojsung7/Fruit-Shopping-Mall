// src/pages/MyPage/Reviews/index.tsx
import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from '@/routes/paths';
import { formatDate } from '@/utils/formatters';
import styled from 'styled-components';
import API from '@/services/api';

interface Review {
    id: number;
    orderId: number;
    fruitId: number;
    fruitName: string;
    fruitImageUrl: string;
    rating: number;
    content: string;
    reviewDate: string;
    imageUrl?: string;
}

const ReviewsContainer = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const EmptyReviews = styled.div`
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

const ReviewList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
`;

const ReviewItem = styled.div`
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
`;

const ReviewHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #eee;
  
  @media (max-width: 576px) {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
`;

const ReviewDate = styled.div`
  font-weight: 600;
`;

const StarRating = styled.div`
  display: flex;
  align-items: center;
`;

const Star = styled.span<{ filled: boolean }>`
  color: ${props => props.filled ? '#FFD700' : '#ccc'};
  font-size: 20px;
`;

const ReviewContent = styled.div`
  display: flex;
  padding: 24px;
  gap: 16px;
  
  @media (max-width: 576px) {
    flex-direction: column;
  }
`;

const ProductInfo = styled.div`
  display: flex;
  gap: 16px;
  min-width: 250px;
  
  @media (max-width: 576px) {
    margin-bottom: 16px;
  }
`;

const ProductImage = styled.div`
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ProductDetails = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

const ProductName = styled(Link)`
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  
  &:hover {
    color: #4caf50;
  }
`;

const ReviewDetails = styled.div`
  flex: 1;
`;

const ReviewText = styled.p`
  line-height: 1.6;
  margin-bottom: 16px;
`;

const ReviewImage = styled.div`
  margin-top: 16px;
  
  img {
    max-width: 100%;
    max-height: 300px;
    border-radius: 4px;
  }
`;

const ReviewFooter = styled.div`
  display: flex;
  justify-content: flex-end;
  padding: 0 24px 24px;
  gap: 8px;
`;

const ActionButton = styled(Link)`
  padding: 8px 16px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #333;
  font-weight: 500;
  
  &:hover {
    background-color: #eee;
  }
`;

const DeleteButton = styled.button`
  padding: 8px 16px;
  background-color: transparent;
  border: 1px solid #f44336;
  border-radius: 4px;
  color: #f44336;
  font-weight: 500;
  cursor: pointer;
  
  &:hover {
    background-color: #ffebee;
  }
`;

const ShopButton = styled(Link)`
  display: inline-block;
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border-radius: 4px;
  font-weight: 600;
  margin-top: 16px;
  
  &:hover {
    background-color: #388e3c;
  }
`;

const LoadingContainer = styled.div`
  text-align: center;
  padding: 40px;
`;

const MyReviewsPage = () => {
    const navigate = useNavigate();
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    const [reviews, setReviews] = useState<Review[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        const fetchReviews = async () => {
            try {
                setLoading(true);
                const response = await API.get('/reviews/my-reviews');
                setReviews(response.data);
            } catch (error) {
                console.error('리뷰를 불러오는데 실패했습니다:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchReviews();
    }, [isAuthenticated, navigate]);

    const handleDeleteReview = async (id: number) => {
        if (!window.confirm('리뷰를 삭제하시겠습니까?')) {
            return;
        }

        try {
            await API.delete(`/reviews/${id}`);
            setReviews(reviews.filter(review => review.id !== id));
            alert('리뷰가 삭제되었습니다.');
        } catch (error) {
            console.error('리뷰 삭제에 실패했습니다:', error);
            alert('리뷰 삭제에 실패했습니다. 다시 시도해주세요.');
        }
    };

    if (loading) {
        return <LoadingContainer>리뷰를 불러오는 중...</LoadingContainer>;
    }

    if (reviews.length === 0) {
        return (
            <ReviewsContainer>
                <PageTitle>내 리뷰</PageTitle>
                <EmptyReviews>
                    <h2>작성한 리뷰가 없습니다.</h2>
                    <ShopButton to={PATHS.PRODUCTS}>쇼핑하러 가기</ShopButton>
                </EmptyReviews>
            </ReviewsContainer>
        );
    }

    return (
        <ReviewsContainer>
            <PageTitle>내 리뷰</PageTitle>

            <ReviewList>
                {reviews.map((review) => (
                    <ReviewItem key={review.id}>
                        <ReviewHeader>
                            <ReviewDate>{formatDate(review.reviewDate)}</ReviewDate>
                            <StarRating>
                                {[1, 2, 3, 4, 5].map((star) => (
                                    <Star key={star} filled={star <= review.rating}>★</Star>
                                ))}
                            </StarRating>
                        </ReviewHeader>

                        <ReviewContent>
                            <ProductInfo>
                                <ProductImage>
                                    <img
                                        src={review.fruitImageUrl || '/images/placeholder-fruit.jpg'}
                                        alt={review.fruitName}
                                    />
                                </ProductImage>
                                <ProductDetails>
                                    <ProductName to={PATHS.PRODUCT_DETAIL(review.fruitId)}>
                                        {review.fruitName}
                                    </ProductName>
                                    <div>주문번호: {review.orderId}</div>
                                </ProductDetails>
                            </ProductInfo>

                            <ReviewDetails>
                                <ReviewText>{review.content}</ReviewText>
                                {review.imageUrl && (
                                    <ReviewImage>
                                        <img src={review.imageUrl} alt="리뷰 이미지" />
                                    </ReviewImage>
                                )}
                            </ReviewDetails>
                        </ReviewContent>

                        <ReviewFooter>
                            <ActionButton to={`/review/edit/${review.id}`}>
                                수정
                            </ActionButton>
                            <DeleteButton onClick={() => handleDeleteReview(review.id)}>
                                삭제
                            </DeleteButton>
                        </ReviewFooter>
                    </ReviewItem>
                ))}
            </ReviewList>
        </ReviewsContainer>
    );
};

export default MyReviewsPage;