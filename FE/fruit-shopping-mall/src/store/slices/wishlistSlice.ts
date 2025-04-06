// src/store/slices/wishlistSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import API from '@/services/api';

export interface WishlistItem {
    id: number;
    memberId: number;
    fruitId: number;
    fruitName: string;
    fruitImageUrl: string;
    fruitPrice: number;
    stockQuantity: number;
    origin: string;
    season: string;
    addedDate: string;
}

interface WishlistState {
    items: WishlistItem[];
    totalItems: number;
    loading: boolean;
    error: string | null;
}

const initialState: WishlistState = {
    items: [],
    totalItems: 0,
    loading: false,
    error: null
};

// 위시리스트 조회 액션
export const fetchWishlist = createAsyncThunk(
    'wishlist/fetchWishlist',
    async (_, { rejectWithValue }) => {
        try {
            const response = await API.get('/wishlist');
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '위시리스트를 불러오는데 실패했습니다.');
        }
    }
);

// 위시리스트에 상품 추가 액션
export const addToWishlist = createAsyncThunk(
    'wishlist/addToWishlist',
    async (fruitId: number, { rejectWithValue }) => {
        try {
            const response = await API.post('/wishlist', { fruitId });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '위시리스트에 상품을 추가하는데 실패했습니다.');
        }
    }
);

// 위시리스트에서 상품 삭제 액션
export const removeFromWishlist = createAsyncThunk(
    'wishlist/removeFromWishlist',
    async (id: number, { rejectWithValue }) => {
        try {
            await API.delete(`/wishlist/${id}`);
            return id;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '위시리스트에서 상품을 삭제하는데 실패했습니다.');
        }
    }
);

// 위시리스트 비우기 액션
export const clearWishlist = createAsyncThunk(
    'wishlist/clearWishlist',
    async (_, { rejectWithValue }) => {
        try {
            await API.delete('/wishlist');
            return true;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '위시리스트를 비우는데 실패했습니다.');
        }
    }
);

const wishlistSlice = createSlice({
    name: 'wishlist',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            // 위시리스트 조회
            .addCase(fetchWishlist.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchWishlist.fulfilled, (state, action) => {
                state.loading = false;
                state.items = action.payload.wishlistItems || [];
                state.totalItems = action.payload.totalItems || 0;
            })
            .addCase(fetchWishlist.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // 위시리스트에 상품 추가
            .addCase(addToWishlist.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(addToWishlist.fulfilled, (state, action) => {
                state.loading = false;
                // 이미 있는 상품인지 확인
                const existingItemIndex = state.items.findIndex(item => item.id === action.payload.id);
                if (existingItemIndex === -1) {
                    state.items.push(action.payload);
                    state.totalItems++;
                }
            })
            .addCase(addToWishlist.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // 위시리스트에서 상품 삭제
            .addCase(removeFromWishlist.fulfilled, (state, action) => {
                state.items = state.items.filter(item => item.id !== action.payload);
                state.totalItems = state.items.length;
            })
            // 위시리스트 비우기
            .addCase(clearWishlist.fulfilled, (state) => {
                state.items = [];
                state.totalItems = 0;
            });
    }
});

export default wishlistSlice.reducer;