import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import { PATHS } from './paths';
import PrivateRoute from './PrivateRoute';
import AdminRoute from './AdminRoute';

// 레이아웃
import MainLayout from '@/components/layout/MainLayout';

// 비동기 로드할 페이지들
const HomePage = lazy(() => import('@/pages/Home'));
const LoginPage = lazy(() => import('@/pages/Auth/Login'));
const RegisterPage = lazy(() => import('@/pages/Auth/Register'));
const ProductListPage = lazy(() => import('@/pages/Product/ProductListPage'));
const ProductDetailPage = lazy(() => import('@/pages/Product/ProductDetailPage'));
const CategoryPage = lazy(() => import('@/pages/Product/CategoryPage'));
const CartPage = lazy(() => import('@/pages/Cart'));
const WishlistPage = lazy(() => import('@/pages/Wishlist'));
const CheckoutPage = lazy(() => import('@/pages/Order/CheckoutPage'));
const OrderCompletePage = lazy(() => import('@/pages/Order/OrderCompletePage'));
const MyPage = lazy(() => import('@/pages/MyPage/Profile'));
const MyOrdersPage = lazy(() => import('@/pages/MyPage/Orders'));
const MyReviewsPage = lazy(() => import('@/pages/MyPage/Reviews'));
const NotFoundPage = lazy(() => import('@/pages/NotFound'));

const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route element={<MainLayout />}>
                        {/* 공개 경로 */}
                        <Route path={PATHS.HOME} element={<HomePage />} />
                        <Route path={PATHS.LOGIN} element={<LoginPage />} />
                        <Route path={PATHS.REGISTER} element={<RegisterPage />} />
                        <Route path={PATHS.PRODUCTS} element={<ProductListPage />} />
                        <Route path={PATHS.PRODUCT_DETAIL()} element={<ProductDetailPage />} />
                        <Route path={PATHS.CATEGORY()} element={<CategoryPage />} />

                        {/* 인증 필요 경로 */}
                        <Route element={<PrivateRoute />}>
                            <Route path={PATHS.CART} element={<CartPage />} />
                            <Route path={PATHS.WISHLIST} element={<WishlistPage />} />
                            <Route path={PATHS.CHECKOUT} element={<CheckoutPage />} />
                            <Route path={PATHS.ORDER_COMPLETE} element={<OrderCompletePage />} />
                            <Route path={PATHS.MY_PAGE} element={<MyPage />} />
                            <Route path={PATHS.MY_ORDERS} element={<MyOrdersPage />} />
                            <Route path={PATHS.MY_REVIEWS} element={<MyReviewsPage />} />
                        </Route>

                        {/* 404 페이지 */}
                        <Route path={PATHS.NOT_FOUND} element={<NotFoundPage />} />
                    </Route>
                </Routes>
            </Suspense>
        </BrowserRouter>
    );
};

export default AppRoutes;