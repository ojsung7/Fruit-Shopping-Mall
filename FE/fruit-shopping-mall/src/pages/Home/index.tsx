// src/pages/Home/index.tsx
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchProducts } from '@/store/slices/productSlice';
import { PATHS } from '@/routes/paths';
import ProductCard from '@/components/product/ProductCard';
import styled from 'styled-components';

const HeroSection = styled.section`
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('/images/fruits-banner.jpg');
  background-size: cover;
  background-position: center;
  color: white;
  text-align: center;
  padding: 60px 20px;
  margin-bottom: 40px;
  border-radius: 8px;
  
  @media (min-width: 768px) {
    padding: 100px 20px;
  }
`;

const HeroTitle = styled.h1`
  font-size: 28px;
  margin-bottom: 16px;
  
  @media (min-width: 768px) {
    font-size: 40px;
  }
`;

const HeroSubtitle = styled.p`
  font-size: 16px;
  margin-bottom: 24px;
  
  @media (min-width: 768px) {
    font-size: 20px;
  }
`;

const HeroButton = styled(Link)`
  display: inline-block;
  background-color: #4caf50;
  color: white;
  padding: 12px 24px;
  border-radius: 4px;
  font-weight: 600;
  &:hover {
    background-color: #388e3c;
  }
`;

const SectionTitle = styled.h2`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
  text-align: center;
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 24px;
  margin-bottom: 40px;
  
  @media (min-width: 640px) {
    grid-template-columns: repeat(2, 1fr);
  }
  
  @media (min-width: 768px) {
    grid-template-columns: repeat(3, 1fr);
  }
  
  @media (min-width: 1024px) {
    grid-template-columns: repeat(4, 1fr);
  }
`;

const SectionLink = styled(Link)`
  display: block;
  text-align: center;
  color: #4caf50;
  font-weight: 500;
  margin-bottom: 40px;
  &:hover {
    text-decoration: underline;
  }
`;

const FeaturesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
`;

const FeatureCard = styled.div`
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
`;

const FeatureIcon = styled.div`
  font-size: 40px;
  margin-bottom: 16px;
  color: #4caf50;
`;

const FeatureTitle = styled.h3`
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
`;

const HomePage = () => {
  const dispatch = useAppDispatch();
  const { products, loading } = useAppSelector((state) => state.product);

  useEffect(() => {
    dispatch(fetchProducts());
  }, [dispatch]);

  // 신상품 6개 선택
  const newProducts = [...products]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 6);

  // 제철 과일 선택
  const seasonalFruits = products.filter(product => {
    const currentMonth = new Date().getMonth() + 1; // 0-based index

    // 계절에 따른 필터링 (간단한 예시)
    if (currentMonth >= 3 && currentMonth <= 5) { // 봄
      return product.season === '봄';
    } else if (currentMonth >= 6 && currentMonth <= 8) { // 여름
      return product.season === '여름';
    } else if (currentMonth >= 9 && currentMonth <= 11) { // 가을
      return product.season === '가을';
    } else { // 겨울
      return product.season === '겨울';
    }
  }).slice(0, 6);

  return (
    <div>
      <HeroSection>
        <HeroTitle>신선한 과일을 집에서 편하게</HeroTitle>
        <HeroSubtitle>엄선된 최고 품질의 과일을 합리적인 가격으로 만나보세요</HeroSubtitle>
        <HeroButton to={PATHS.PRODUCTS}>쇼핑하기</HeroButton>
      </HeroSection>

      <section>
        <SectionTitle>신상품</SectionTitle>
        {loading ? (
          <p>로딩 중...</p>
        ) : (
          <>
            <ProductGrid>
              {newProducts.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </ProductGrid>
            <SectionLink to={PATHS.PRODUCTS}>모든 상품 보기</SectionLink>
          </>
        )}
      </section>

      <section>
        <SectionTitle>제철 과일</SectionTitle>
        {loading ? (
          <p>로딩 중...</p>
        ) : (
          <>
            <ProductGrid>
              {seasonalFruits.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </ProductGrid>
            <SectionLink to={PATHS.PRODUCTS}>제철 과일 더 보기</SectionLink>
          </>
        )}
      </section>

      <section>
        <SectionTitle>왜 과일마켓인가요?</SectionTitle>
        <FeaturesGrid>
          <FeatureCard>
            <FeatureIcon>🚚</FeatureIcon>
            <FeatureTitle>신속한 배송</FeatureTitle>
            <p>오전 11시 이전 주문 시 당일 출고, 신선함을 빠르게</p>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>✅</FeatureIcon>
            <FeatureTitle>엄격한 품질 관리</FeatureTitle>
            <p>까다로운 기준으로 선별한 최상급 과일만 제공</p>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>💰</FeatureIcon>
            <FeatureTitle>합리적인 가격</FeatureTitle>
            <p>중간 유통 단계를 줄여 더 저렴하게</p>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>🔄</FeatureIcon>
            <FeatureTitle>만족도 보장</FeatureTitle>
            <p>맛과 신선도에 문제 있을 시 100% 환불</p>
          </FeatureCard>
        </FeaturesGrid>
      </section>
    </div>
  );
};

export default HomePage;