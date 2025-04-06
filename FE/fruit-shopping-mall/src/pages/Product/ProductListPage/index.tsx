// src/pages/Product/ProductListPage/index.tsx
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchProducts } from '@/store/slices/productSlice';
import ProductCard from '@/components/product/ProductCard';
import styled from 'styled-components';

const Container = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 16px;
`;

const FilterContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 24px;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const FilterGroup = styled.div`
  display: flex;
  flex-direction: column;
  min-width: 200px;
  flex: 1;
`;

const FilterLabel = styled.label`
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
`;

const FilterSelect = styled.select`
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const PriceRangeContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const PriceInput = styled.input`
  width: 100px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const FilterButton = styled.button`
  padding: 8px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  margin-top: auto;
  
  &:hover {
    background-color: #388e3c;
  }
`;

const SearchResults = styled.div`
  margin-bottom: 16px;
  font-size: 16px;
  color: #666;
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
`;

const EmptyResults = styled.div`
  text-align: center;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  
  h2 {
    font-size: 20px;
    margin-bottom: 16px;
    color: #666;
  }
  
  p {
    color: #888;
    margin-bottom: 16px;
  }
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 32px;
`;

const PaginationButton = styled.button<{ isActive?: boolean }>`
  width: 36px;
  height: 36px;
  margin: 0 4px;
  border: 1px solid ${props => props.isActive ? '#4caf50' : '#ddd'};
  border-radius: 4px;
  background-color: ${props => props.isActive ? '#4caf50' : 'white'};
  color: ${props => props.isActive ? 'white' : '#333'};
  cursor: pointer;
  
  &:hover {
    background-color: ${props => props.isActive ? '#388e3c' : '#f5f5f5'};
  }
  
  &:disabled {
    background-color: #f5f5f5;
    color: #ccc;
    cursor: not-allowed;
  }
`;

const LoadingContainer = styled.div`
  text-align: center;
  padding: 40px;
`;

const ProductListPage = () => {
    const dispatch = useAppDispatch();
    const { products, loading } = useAppSelector((state) => state.product);
    const [searchParams, setSearchParams] = useSearchParams();

    // 필터 상태
    const [category, setCategory] = useState('');
    const [origin, setOrigin] = useState('');
    const [season, setSeason] = useState('');
    const [minPrice, setMinPrice] = useState('');
    const [maxPrice, setMaxPrice] = useState('');
    const [sortBy, setSortBy] = useState('newest');
    const [currentPage, setCurrentPage] = useState(1);

    // 검색 키워드
    const searchKeyword = searchParams.get('search') || '';

    useEffect(() => {
        dispatch(fetchProducts());
    }, [dispatch]);

    // URL 검색 파라미터가 변경되면 필터 상태 업데이트
    useEffect(() => {
        const searchCategory = searchParams.get('category') || '';
        const searchOrigin = searchParams.get('origin') || '';
        const searchSeason = searchParams.get('season') || '';
        const searchMinPrice = searchParams.get('minPrice') || '';
        const searchMaxPrice = searchParams.get('maxPrice') || '';
        const searchSortBy = searchParams.get('sortBy') || 'newest';
        const page = searchParams.get('page') || '1';

        setCategory(searchCategory);
        setOrigin(searchOrigin);
        setSeason(searchSeason);
        setMinPrice(searchMinPrice);
        setMaxPrice(searchMaxPrice);
        setSortBy(searchSortBy);
        setCurrentPage(parseInt(page));
    }, [searchParams]);

    // 필터 적용
    const handleApplyFilters = () => {
        const params: Record<string, string> = {};

        if (category) params.category = category;
        if (origin) params.origin = origin;
        if (season) params.season = season;
        if (minPrice) params.minPrice = minPrice;
        if (maxPrice) params.maxPrice = maxPrice;
        if (sortBy !== 'newest') params.sortBy = sortBy;
        if (searchKeyword) params.search = searchKeyword;

        setSearchParams(params);
    };

    // 페이징 처리
    const handlePageChange = (page: number) => {
        searchParams.set('page', page.toString());
        setSearchParams(searchParams);
    };

    // 필터링된 상품 목록
    let filteredProducts = [...products];

    // 카테고리 필터
    if (category) {
        filteredProducts = filteredProducts.filter(
            product => product.categoryId.toString() === category
        );
    }

    // 원산지 필터
    if (origin) {
        filteredProducts = filteredProducts.filter(
            product => product.origin === origin
        );
    }

    // 제철 필터
    if (season) {
        filteredProducts = filteredProducts.filter(
            product => product.season === season
        );
    }

    // 가격 범위 필터
    if (minPrice) {
        filteredProducts = filteredProducts.filter(
            product => product.price >= parseInt(minPrice)
        );
    }

    if (maxPrice) {
        filteredProducts = filteredProducts.filter(
            product => product.price <= parseInt(maxPrice)
        );
    }

    // 검색어 필터
    if (searchKeyword) {
        filteredProducts = filteredProducts.filter(
            product => product.fruitName.toLowerCase().includes(searchKeyword.toLowerCase())
        );
    }

    // 정렬
    if (sortBy === 'priceAsc') {
        filteredProducts.sort((a, b) => a.price - b.price);
    } else if (sortBy === 'priceDesc') {
        filteredProducts.sort((a, b) => b.price - a.price);
    } else if (sortBy === 'nameAsc') {
        filteredProducts.sort((a, b) => a.fruitName.localeCompare(b.fruitName));
    } else if (sortBy === 'nameDesc') {
        filteredProducts.sort((a, b) => b.fruitName.localeCompare(a.fruitName));
    } else {
        // 기본 최신순
        filteredProducts.sort((a, b) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
    }

    // 페이징
    const productsPerPage = 12;
    const totalPages = Math.ceil(filteredProducts.length / productsPerPage);
    const startIndex = (currentPage - 1) * productsPerPage;
    const paginatedProducts = filteredProducts.slice(startIndex, startIndex + productsPerPage);

    // 고유한 원산지 목록
    const originOptions = Array.from(new Set(products.map(product => product.origin)))
        .filter(origin => origin)
        .sort();

    // 고유한 계절 목록
    const seasonOptions = Array.from(new Set(products.map(product => product.season)))
        .filter(season => season)
        .sort();

    // 고유한 카테고리 목록
    const categoryOptions = Array.from(
        new Set(products.map(product => ({ id: product.categoryId, name: product.categoryName })))
    ).sort((a, b) => a.name.localeCompare(b.name));

    if (loading) {
        return <LoadingContainer>상품을 불러오는 중...</LoadingContainer>;
    }

    return (
        <Container>
            <PageTitle>
                {searchKeyword ? `'${searchKeyword}' 검색 결과` : '전체 상품'}
            </PageTitle>

            <FilterContainer>
                <FilterGroup>
                    <FilterLabel>카테고리</FilterLabel>
                    <FilterSelect
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                    >
                        <option value="">전체</option>
                        {categoryOptions.map((cat) => (
                            <option key={cat.id} value={cat.id}>
                                {cat.name}
                            </option>
                        ))}
                    </FilterSelect>
                </FilterGroup>

                <FilterGroup>
                    <FilterLabel>원산지</FilterLabel>
                    <FilterSelect
                        value={origin}
                        onChange={(e) => setOrigin(e.target.value)}
                    >
                        <option value="">전체</option>
                        {originOptions.map((orig) => (
                            <option key={orig} value={orig}>
                                {orig}
                            </option>
                        ))}
                    </FilterSelect>
                </FilterGroup>

                <FilterGroup>
                    <FilterLabel>제철</FilterLabel>
                    <FilterSelect
                        value={season}
                        onChange={(e) => setSeason(e.target.value)}
                    >
                        <option value="">전체</option>
                        {seasonOptions.map((seas) => (
                            <option key={seas} value={seas}>
                                {seas}
                            </option>
                        ))}
                    </FilterSelect>
                </FilterGroup>

                <FilterGroup>
                    <FilterLabel>가격 범위</FilterLabel>
                    <PriceRangeContainer>
                        <PriceInput
                            type="number"
                            placeholder="최소"
                            value={minPrice}
                            onChange={(e) => setMinPrice(e.target.value)}
                        />
                        <span>~</span>
                        <PriceInput
                            type="number"
                            placeholder="최대"
                            value={maxPrice}
                            onChange={(e) => setMaxPrice(e.target.value)}
                        />
                    </PriceRangeContainer>
                </FilterGroup>

                <FilterGroup>
                    <FilterLabel>정렬</FilterLabel>
                    <FilterSelect
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value)}
                    >
                        <option value="newest">최신순</option>
                        <option value="priceAsc">가격 낮은순</option>
                        <option value="priceDesc">가격 높은순</option>
                        <option value="nameAsc">이름 오름차순</option>
                        <option value="nameDesc">이름 내림차순</option>
                    </FilterSelect>
                </FilterGroup>

                <FilterGroup>
                    <FilterButton onClick={handleApplyFilters}>
                        필터 적용
                    </FilterButton>
                </FilterGroup>
            </FilterContainer>

            <SearchResults>
                총 {filteredProducts.length}개의 상품
            </SearchResults>

            {paginatedProducts.length === 0 ? (
                <EmptyResults>
                    <h2>검색 결과가 없습니다</h2>
                    <p>다른 검색어나 필터 조건을 시도해보세요.</p>
                </EmptyResults>
            ) : (
                <ProductGrid>
                    {paginatedProducts.map((product) => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </ProductGrid>
            )}

            {totalPages > 1 && (
                <PaginationContainer>
                    <PaginationButton
                        onClick={() => handlePageChange(1)}
                        disabled={currentPage === 1}
                    >
                        &laquo;
                    </PaginationButton>

                    <PaginationButton
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                    >
                        &lt;
                    </PaginationButton>

                    {Array.from({ length: totalPages }, (_, i) => i + 1)
                        .filter(page => (
                            page === 1 ||
                            page === totalPages ||
                            (page >= currentPage - 1 && page <= currentPage + 1)
                        ))
                        .map((page, index, array) => (
                            <React.Fragment key={page}>
                                {index > 0 && array[index - 1] !== page - 1 && (
                                    <span style={{ margin: '0 8px' }}>...</span>
                                )}
                                <PaginationButton
                                    isActive={currentPage === page}
                                    onClick={() => handlePageChange(page)}
                                >
                                    {page}
                                </PaginationButton>
                            </React.Fragment>
                        ))
                    }

                    <PaginationButton
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage === totalPages}
                    >
                        &gt;
                    </PaginationButton>

                    <PaginationButton
                        onClick={() => handlePageChange(totalPages)}
                        disabled={currentPage === totalPages}
                    >
                        &raquo;
                    </PaginationButton>
                </PaginationContainer>
            )}
        </Container>
    );
};

export default ProductListPage;