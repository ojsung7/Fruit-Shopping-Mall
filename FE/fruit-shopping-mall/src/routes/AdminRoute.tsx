// src/routes/AdminRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from './paths';

const AdminRoute = () => {
    const { isAuthenticated, user } = useAppSelector((state) => state.auth);

    // 로그인 상태 확인 및 사용자가 관리자 역할을 가지고 있는지 확인
    const isAdmin = user?.roles?.includes('ROLE_ADMIN');

    // 인증되지 않았거나 관리자가 아니면 홈페이지로 리디렉션
    if (!isAuthenticated || !isAdmin) {
        return <Navigate to={PATHS.HOME} />;
    }

    // 관리자인 경우 자식 라우트 렌더링
    return <Outlet />;
};

export default AdminRoute;