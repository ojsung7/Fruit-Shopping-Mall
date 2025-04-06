// src/pages/Product/CategoryPage/index.tsx
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchProducts } from '@/store/slices/productSlice';
import ProductCard from '@/components/Product/ProductCard';
import styled from 'styled-components';
import API from '@/services/api';

interface Category {
    id: number;
    name: string;
    description: string;
}

const Container = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
`;

const EmptyMessage = styled.div`
  text-align: center;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const LoadingContainer = styled.div`
  text-align: center;
  padding: 40px;
`;

const CategoryPage = () => {
    const { id } = useParams<{ id: string }>();
    const dispatch = useAppDispatch();
    const { products, loading } = useAppSelector((state) => state.product);
    const [category, setCategory] = useState<Category | null>(null);
    const [categoryLoading, setCategoryLoading] = useState(true);

    useEffect(() => {
        // 카테고리 정보 불러오기
        const fetchCategory = async () => {
            try {
                setCategoryLoading(true);
                const response = await API.get(`/categories/${id}`);
                setCategory(response.data);
            } catch (error) {
                console.error('카테고리 정보를 불러오는데 실패했습니다:', error);
            } finally {
                setCategoryLoading(false);
            }
        };

        fetchCategory();
        dispatch(fetchProducts());
    }, [id, dispatch]);

    // 카테고리에 해당하는 상품 필터링
    const categoryProducts = products.filter(
        product => product.categoryId === Number(id)
    );

    if (loading || categoryLoading) {
        return <LoadingContainer>로딩 중...</LoadingContainer>;
    }

    return (
        <Container>
            <PageTitle>{category?.name || '카테고리'}</PageTitle>
            {category?.description && <p>{category.description}</p>}

            {categoryProducts.length === 0 ? (
                <EmptyMessage>
                    <h2>이 카테고리에 상품이 없습니다.</h2>
                </EmptyMessage>
            ) : (
                <ProductGrid>
                    {categoryProducts.map((product) => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </ProductGrid>
            )}
        </Container>
    );
};

export default CategoryPage;