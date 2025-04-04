import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import config from '@/config/config';
import { getItem } from '@/utils/localStorage';

// Axios 인스턴스 생성
const API: AxiosInstance = axios.create({
    baseURL: config.apiUrl,
    timeout: config.apiTimeout,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 요청 인터셉터
API.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = getItem<string>('authToken', '');
        if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
  }
);

// 응답 인터셉터
API.interceptors.response.use(
    (response: AxiosResponse) => {
        return response;
    },
    (error) => {
        // 401 에러 처리 (인증 만료)
        if (error.response && error.response.status === 401) {
        // 로그아웃 처리 또는 토큰 갱신 로직
        console.log('인증이 만료되었습니다.');
        }
        return Promise.reject(error);
    }
);

export default API;