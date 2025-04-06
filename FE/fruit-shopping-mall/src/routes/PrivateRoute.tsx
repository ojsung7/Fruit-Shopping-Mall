// src/routes/PrivateRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from './paths';

const PrivateRoute = () => {
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    return isAuthenticated ? <Outlet /> : <Navigate to={PATHS.LOGIN} />;
};

export default PrivateRoute;