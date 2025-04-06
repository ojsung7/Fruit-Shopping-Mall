import { useEffect } from 'react';
import { useAppDispatch } from './store/hooks'; // useDispatch 대신 useAppDispatch 사용
import { fetchCurrentUser } from './store/slices/authSlice';
import AppRoutes from './routes';
import { getItem } from './utils/localStorage';

function AppContent() {
    const dispatch = useAppDispatch(); // 타입이 지정된 디스패치 훅 사용

    useEffect(() => {
        // 토큰이 있으면 사용자 정보 로드
        const token = getItem('authToken', null);
        if (token) {
            dispatch(fetchCurrentUser());
        }
    }, [dispatch]);

    return <AppRoutes />;
}

export default AppContent;