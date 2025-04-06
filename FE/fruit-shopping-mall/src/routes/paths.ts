// src/routes/paths.ts

/**
 * 애플리케이션 내 모든 경로를 한 곳에서 관리하는 객체
 * 경로 변경 시 이 파일만 수정하면 되도록 구성
 */
export const PATHS = {
    // 공통 페이지
    HOME: '/',

    // 인증 관련
    LOGIN: '/login',
    REGISTER: '/register',

    // 상품 관련
    PRODUCTS: '/products',
    PRODUCT_DETAIL: (id?: string | number) => id ? `/products/${id}` : '/products/:id',
    CATEGORY: (id?: string | number) => id ? `/category/${id}` : '/category/:id',

    // 장바구니 & 위시리스트
    CART: '/cart',
    WISHLIST: '/wishlist',

    // 주문 관련
    CHECKOUT: '/checkout',
    ORDER_COMPLETE: '/order-complete',

    // 마이페이지
    MY_PAGE: '/my-page',
    MY_ORDERS: '/my-page/orders',
    MY_REVIEWS: '/my-page/reviews',
    MY_PROFILE: '/my-page/profile',

    // 관리자 페이지
    ADMIN: {
        DASHBOARD: '/admin',
        PRODUCTS: '/admin/products',
        PRODUCT_CREATE: '/admin/products/create',
        PRODUCT_EDIT: (id?: string | number) => id ? `/admin/products/edit/${id}` : '/admin/products/edit/:id',
        ORDERS: '/admin/orders',
        ORDER_DETAIL: (id?: string | number) => id ? `/admin/orders/${id}` : '/admin/orders/:id',
        MEMBERS: '/admin/members',
        MEMBER_DETAIL: (id?: string | number) => id ? `/admin/members/${id}` : '/admin/members/:id',
        DELIVERIES: '/admin/deliveries',
        CATEGORIES: '/admin/categories',
    },

    // 기타
    NOT_FOUND: '*',
};

// 경로 유틸리티 함수
export const createPath = (path: string, params?: Record<string, string | number>): string => {
    if (!params) return path;

    let result = path;
    Object.entries(params).forEach(([key, value]) => {
        result = result.replace(`:${key}`, String(value));
    });

    return result;
};