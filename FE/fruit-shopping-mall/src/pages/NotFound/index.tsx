// src/pages/NotFound/index.tsx
import { Link } from 'react-router-dom';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
  padding: 20px;
`;

const Title = styled.h1`
  font-size: 100px;
  font-weight: 700;
  color: #4caf50;
  margin-bottom: 16px;
`;

const Subtitle = styled.h2`
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
`;

const Message = styled.p`
  font-size: 18px;
  color: #666;
  margin-bottom: 32px;
  max-width: 500px;
`;

const HomeButton = styled(Link)`
  padding: 12px 24px;
  background-color: #4caf50;
  color: white;
  border-radius: 4px;
  font-weight: 600;
  text-decoration: none;
  
  &:hover {
    background-color: #388e3c;
  }
`;

const NotFoundPage = () => {
    return (
        <Container>
            <Title>404</Title>
            <Subtitle>페이지를 찾을 수 없습니다</Subtitle>
            <Message>
                찾으시는 페이지가 존재하지 않거나, 삭제되었거나,
                주소가 변경되었을 수 있습니다.
            </Message>
            <HomeButton to={PATHS.HOME}>홈으로 돌아가기</HomeButton>
        </Container>
    );
};

export default NotFoundPage;