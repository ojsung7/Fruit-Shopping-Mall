import API from './api';
import { setItem, removeItem } from '@/utils/localStorage';

export interface LoginRequest {
    usernameOrEmail: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
    name: string;
    phoneNumber: string;
    birthDate?: string;
    address?: string;
}

export interface TokenResponse {
    token: string;
    tokenType: string;
    expiresIn: number;
}

export interface UserResponse {
    id: number;
    username: string;
    email: string;
    name: string;
    roles: string[];
}

// 로그인
export const login = async (credentials: LoginRequest): Promise<TokenResponse> => {
    const response = await API.post<TokenResponse>('/auth/login', credentials);
    if (response.data.token) {
        setItem('authToken', response.data.token);
    }
    return response.data;
};

// 회원가입
export const register = async (userData: RegisterRequest): Promise<UserResponse> => {
    const response = await API.post<UserResponse>('/auth/register', userData);
    return response.data;
};

// 로그아웃
export const logout = (): void => {
    removeItem('authToken');
};

// 내 정보 가져오기
export const getCurrentUser = async (): Promise<UserResponse> => {
    const response = await API.get<UserResponse>('/members/me');
    return response.data;
};