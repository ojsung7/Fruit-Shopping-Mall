import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { login as apiLogin, getCurrentUser, LoginRequest, UserResponse } from '@/services/authService';
import { getItem } from '@/utils/localStorage';

interface AuthState {
    user: UserResponse | null;
    token: string | null;
    loading: boolean;
    error: string | null;
    isAuthenticated: boolean;
}

const initialState: AuthState = {
    user: null,
    token: getItem<string>('authToken', null),
    loading: false,
    error: null,
    isAuthenticated: !!getItem<string>('authToken', null)
};

// 로그인 비동기 액션
export const loginAsync = createAsyncThunk(
  'auth/login',
  async (credentials: LoginRequest, { rejectWithValue }) => {
    try {
      const response = await apiLogin(credentials);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '로그인에 실패했습니다.');
    }
  }
);

// 사용자 정보 불러오기 비동기 액션
export const fetchCurrentUser = createAsyncThunk(
  'auth/fetchCurrentUser',
  async (_, { rejectWithValue }) => {
    try {
      const response = await getCurrentUser();
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '사용자 정보를 불러오는데 실패했습니다.');
    }
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
      localStorage.removeItem('authToken');
    },
    clearError: (state) => {
      state.error = null;
    }
  },
  extraReducers: (builder) => {
    builder
      // 로그인 요청 중
      .addCase(loginAsync.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      // 로그인 성공
      .addCase(loginAsync.fulfilled, (state, action: PayloadAction<any>) => {
        state.loading = false;
        state.token = action.payload.token;
        state.isAuthenticated = true;
      })
      // 로그인 실패
      .addCase(loginAsync.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // 사용자 정보 요청 중
      .addCase(fetchCurrentUser.pending, (state) => {
        state.loading = true;
      })
      // 사용자 정보 가져오기 성공
      .addCase(fetchCurrentUser.fulfilled, (state, action: PayloadAction<UserResponse>) => {
        state.loading = false;
        state.user = action.payload;
      })
      // 사용자 정보 가져오기 실패
      .addCase(fetchCurrentUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  }
});

export const { logout, clearError } = authSlice.actions;
export default authSlice.reducer;