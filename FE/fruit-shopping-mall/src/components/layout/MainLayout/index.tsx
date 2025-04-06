// src/components/layout/MainLayout/index.tsx
import { Outlet } from 'react-router-dom';
import Header from '../Header';
import Footer from '../Footer';
import styled from 'styled-components';

const LayoutContainer = styled.div`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
`;

const Main = styled.main`
  flex: 1;
  padding: 20px 0;
`;

const MainLayout = () => {
    return (
        <LayoutContainer>
            <Header />
            <Main className="container">
                <Outlet />
            </Main>
            <Footer />
        </LayoutContainer>
    );
};

export default MainLayout;