// src/App.tsx
import { Provider } from 'react-redux';
import styled from 'styled-components';
import store from './store';
import AppContent from './AppContent';

// 전체 애플리케이션 컨테이너 스타일
const AppContainer = styled.div`
  width: 100%;
  min-height: 100vh;
`;

function App() {
  return (
    <Provider store={store}>
      <AppContainer>
        <AppContent />
      </AppContainer>
    </Provider>
  );
}

export default App;