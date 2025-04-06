// src/components/layout/Header/index.tsx
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppSelector, useAppDispatch } from '@/store/hooks';
import { logout } from '@/store/slices/authSlice';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

const HeaderContainer = styled.header`
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 16px 0;
`;

const HeaderContent = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const Logo = styled(Link)`
  font-size: 24px;
  font-weight: 700;
  color: #4caf50;
`;

const Nav = styled.nav`
  display: flex;
  gap: 24px;
`;

const NavLink = styled(Link)`
  color: #333;
  font-weight: 500;
  &:hover {
    color: #4caf50;
  }
`;

const SearchBar = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
`;

const SearchInput = styled.input`
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-width: 250px;
`;

const SearchButton = styled.button`
  padding: 8px 16px;
  background-color: #4caf50;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
`;

const UserActions = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
`;

const IconButton = styled(Link)`
  position: relative;
  color: #333;
  font-size: 20px;
  &:hover {
    color: #4caf50;
  }
`;

const Badge = styled.span`
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #f44336;
  color: white;
  border-radius: 50%;
  width: 18px;
  height: 18px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Header = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const { isAuthenticated, user } = useAppSelector((state) => state.auth);
    const { totalItems: cartItems } = useAppSelector((state) => state.cart);
    const { totalItems: wishlistItems } = useAppSelector((state) => state.wishlist);

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        if (searchTerm.trim()) {
            navigate(`${PATHS.PRODUCTS}?search=${searchTerm}`);
        }
    };

    const handleLogout = () => {
        dispatch(logout());
        navigate(PATHS.HOME);
    };

    return (
        <HeaderContainer>
            <div className="container">
                <HeaderContent>
                    <Logo to={PATHS.HOME}>과일마켓</Logo>

                    <Nav>
                        <NavLink to={PATHS.HOME}>홈</NavLink>
                        <NavLink to={PATHS.PRODUCTS}>상품</NavLink>
                        {/* 관리자 메뉴 */}
                        {isAuthenticated && user?.roles?.includes('ROLE_ADMIN') && (
                            <NavLink to={PATHS.ADMIN.DASHBOARD}>관리자</NavLink>
                        )}
                    </Nav>

                    <form onSubmit={handleSearch}>
                        <SearchBar>
                            <SearchInput
                                type="text"
                                placeholder="과일 검색..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                            <SearchButton type="submit">검색</SearchButton>
                        </SearchBar>
                    </form>

                    <UserActions>
                        {isAuthenticated ? (
                            <>
                                <IconButton to={PATHS.WISHLIST}>
                                    ♥
                                    {wishlistItems > 0 && <Badge>{wishlistItems}</Badge>}
                                </IconButton>
                                <IconButton to={PATHS.CART}>
                                    🛒
                                    {cartItems > 0 && <Badge>{cartItems}</Badge>}
                                </IconButton>
                                <NavLink to={PATHS.MY_PAGE}>{user?.name || '마이페이지'}</NavLink>
                                <NavLink to="#" onClick={handleLogout}>로그아웃</NavLink>
                            </>
                        ) : (
                            <>
                                <NavLink to={PATHS.LOGIN}>로그인</NavLink>
                                <NavLink to={PATHS.REGISTER}>회원가입</NavLink>
                            </>
                        )}
                    </UserActions>
                </HeaderContent>
            </div>
        </HeaderContainer>
    );
};

export default Header;