import API from './api';

export interface Product {
    id: number;
    fruitName: string;
    origin: string;
    stockQuantity: number;
    price: number;
    categoryId: number;
    categoryName: string;
    season: string;
    description: string;
    imageUrl: string;
    createdAt: string;
    updatedAt: string;
}

export interface ProductResponse {
    content: Product[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

// 모든 상품 가져오기
export const getAllProducts = async (): Promise<Product[]> => {
    const response = await API.get<Product[]>('/fruits');
    return response.data;
};

// 페이지네이션으로 상품 가져오기
export const getProductsByPage = async (page: number, size: number): Promise<ProductResponse> => {
    const response = await API.get<ProductResponse>('/fruits', {
        params: { page, size }
    });
    return response.data;
};

// 카테고리별 상품 가져오기
export const getProductsByCategory = async (categoryId: number): Promise<Product[]> => {
    const response = await API.get<Product[]>(`/fruits/category/${categoryId}`);
    return response.data;
};

// 상품 상세정보 가져오기
export const getProductById = async (id: number): Promise<Product> => {
    const response = await API.get<Product>(`/fruits/${id}`);
    return response.data;
};

// 상품 검색
export const searchProducts = async (keyword: string): Promise<Product[]> => {
    const response = await API.get<Product[]>(`/fruits/search`, {
        params: { keyword }
    });
    return response.data;
};