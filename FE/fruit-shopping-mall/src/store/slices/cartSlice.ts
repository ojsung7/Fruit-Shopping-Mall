// src/store/slices/cartSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import API from '@/services/api';

export interface CartItem {
    id: number;
    memberId: number;
    fruitId: number;
    fruitName: string;
    fruitImageUrl: string;
    fruitPrice: number;
    quantity: number;
    totalPrice: number;
    addedDate: string;
}

interface CartState {
    items: CartItem[];
    totalItems: number;
    totalPrice: number;
    loading: boolean;
    error: string | null;
}

const initialState: CartState = {
    items: [],
    totalItems: 0,
    totalPrice: 0,
    loading: false,
    error: null
};

// 장바구니 조회 액션
export const fetchCart = createAsyncThunk(
    'cart/fetchCart',
    async (_, { rejectWithValue }) => {
        try {
            const response = await API.get('/cart');
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '장바구니를 불러오는데 실패했습니다.');
        }
    }
);

// 장바구니에 상품 추가 액션
export const addToCart = createAsyncThunk(
    'cart/addToCart',
    async (data: { fruitId: number, quantity: number }, { rejectWithValue }) => {
        try {
            const response = await API.post('/cart', data);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '장바구니에 상품을 추가하는데 실패했습니다.');
        }
    }
);

// 장바구니 상품 수량 변경 액션
export const updateCartItem = createAsyncThunk(
    'cart/updateCartItem',
    async (data: { id: number, quantity: number }, { rejectWithValue }) => {
        try {
            const response = await API.put(`/cart/${data.id}`, { quantity: data.quantity });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '장바구니 수량 변경에 실패했습니다.');
        }
    }
);

// 장바구니 상품 삭제 액션
export const removeFromCart = createAsyncThunk(
    'cart/removeFromCart',
    async (id: number, { rejectWithValue }) => {
        try {
            await API.delete(`/cart/${id}`);
            return id;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '장바구니에서 상품을 삭제하는데 실패했습니다.');
        }
    }
);

// 장바구니 비우기 액션
export const clearCart = createAsyncThunk(
    'cart/clearCart',
    async (_, { rejectWithValue }) => {
        try {
            await API.delete('/cart');
            return true;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || '장바구니를 비우는데 실패했습니다.');
        }
    }
);

const cartSlice = createSlice({
    name: 'cart',
    initialState,
    reducers: {
        calculateTotals: (state) => {
            state.totalItems = state.items.reduce((sum, item) => sum + item.quantity, 0);
            state.totalPrice = state.items.reduce((sum, item) => sum + item.totalPrice, 0);
        }
    },
    extraReducers: (builder) => {
        builder
            // 장바구니 조회
            .addCase(fetchCart.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchCart.fulfilled, (state, action) => {
                state.loading = false;
                state.items = action.payload.cartItems || [];
                state.totalItems = action.payload.totalItems || 0;
                state.totalPrice = action.payload.totalPrice || 0;
            })
            .addCase(fetchCart.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // 장바구니에 상품 추가
            .addCase(addToCart.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(addToCart.fulfilled, (state, action) => {
                state.loading = false;
                // 이미 있는 상품인지 확인
                const existingItemIndex = state.items.findIndex(item => item.id === action.payload.id);
                if (existingItemIndex >= 0) {
                    state.items[existingItemIndex] = action.payload;
                } else {
                    state.items.push(action.payload);
                }
                state.totalItems = state.items.reduce((sum, item) => sum + item.quantity, 0);
                state.totalPrice = state.items.reduce((sum, item) => sum + item.totalPrice, 0);
            })
            .addCase(addToCart.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // 장바구니 상품 수량 변경
            .addCase(updateCartItem.fulfilled, (state, action) => {
                const index = state.items.findIndex(item => item.id === action.payload.id);
                if (index !== -1) {
                    state.items[index] = action.payload;
                    state.totalItems = state.items.reduce((sum, item) => sum + item.quantity, 0);
                    state.totalPrice = state.items.reduce((sum, item) => sum + item.totalPrice, 0);
                }
            })
            // 장바구니 상품 삭제
            .addCase(removeFromCart.fulfilled, (state, action) => {
                state.items = state.items.filter(item => item.id !== action.payload);
                state.totalItems = state.items.reduce((sum, item) => sum + item.quantity, 0);
                state.totalPrice = state.items.reduce((sum, item) => sum + item.totalPrice, 0);
            })
            // 장바구니 비우기
            .addCase(clearCart.fulfilled, (state) => {
                state.items = [];
                state.totalItems = 0;
                state.totalPrice = 0;
            });
    }
});

export const { calculateTotals } = cartSlice.actions;
export default cartSlice.reducer;