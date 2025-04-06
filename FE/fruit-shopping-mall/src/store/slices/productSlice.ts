// src/store/slices/productSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import API from '@/services/api';

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

interface ProductState {
    products: Product[];
    selectedProduct: Product | null;
    loading: boolean;
    error: string | null;
}

const initialState: ProductState = {
    products: [],
    selectedProduct: null,
    loading: false,
    error: null
};

// 모든 상품 가져오기
export const fetchProducts = createAsyncThunk(
    'product/fetchProducts',
    async (_, { rejectWithValue }) => {
        try {
            const response = await API.get('/fruits');
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '상품 목록을 불러오는데 실패했습니다.');
        }
    }
);

// 상품 상세 정보 가져오기
export const fetchProductById = createAsyncThunk(
    'product/fetchProductById',
    async (id: number, { rejectWithValue }) => {
        try {
            const response = await API.get(`/fruits/${id}`);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '상품 정보를 불러오는데 실패했습니다.');
        }
    }
);

const productSlice = createSlice({
    name: 'product',
    initialState,
    reducers: {
        clearSelectedProduct: (state) => {
            state.selectedProduct = null;
        }
    },
    extraReducers: (builder) => {
        builder
            // 상품 목록 가져오기
            .addCase(fetchProducts.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchProducts.fulfilled, (state, action: PayloadAction<Product[]>) => {
                state.loading = false;
                state.products = action.payload;
            })
            .addCase(fetchProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // 상품 상세 가져오기
            .addCase(fetchProductById.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchProductById.fulfilled, (state, action: PayloadAction<Product>) => {
                state.loading = false;
                state.selectedProduct = action.payload;
            })
            .addCase(fetchProductById.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    }
});

export const { clearSelectedProduct } = productSlice.actions;
export default productSlice.reducer;