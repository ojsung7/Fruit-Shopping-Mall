// src/pages/Auth/Login/index.tsx
import { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { loginAsync, clearError } from '@/store/slices/authSlice';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';

interface LoginFormData {
  usernameOrEmail: string;
  password: string;
}

const LoginContainer = styled.div`
  max-width: 400px;
  margin: 40px auto;
  padding: 24px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
  text-align: center;
  color: #333;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
`;

const Input = styled.input`
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const ErrorMessage = styled.p`
  color: #f44336;
  font-size: 14px;
  margin-top: 4px;
`;

const SubmitButton = styled.button`
  padding: 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 8px;
  
  &:hover {
    background-color: #388e3c;
  }
  
  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const RegisterLink = styled.div`
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  
  a {
    color: #4caf50;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
`;

const LoginPage = () => {
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>();
  const [loginError, setLoginError] = useState<string | null>(null);

  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const { loading, error, isAuthenticated } = useAppSelector((state) => state.auth);

  // 리다이렉트 경로 추출
  const from = location.state?.from?.pathname || PATHS.HOME;

  useEffect(() => {
    // 이미 로그인한 경우 리다이렉트
    if (isAuthenticated) {
      navigate(from);
    }

    // 컴포넌트 언마운트 시 에러 초기화
    return () => {
      dispatch(clearError());
    };
  }, [isAuthenticated, navigate, dispatch, from]);

  useEffect(() => {
    if (error) {
      setLoginError(error);
    }
  }, [error]);

  const onSubmit = async (data: LoginFormData) => {
    setLoginError(null);
    await dispatch(loginAsync(data));
  };

  return (
    <LoginContainer>
      <Title>로그인</Title>

      <Form onSubmit={handleSubmit(onSubmit)}>
        <FormGroup>
          <Label htmlFor="usernameOrEmail">아이디 또는 이메일</Label>
          <Input
            id="usernameOrEmail"
            type="text"
            placeholder="아이디 또는 이메일을 입력하세요"
            {...register('usernameOrEmail', {
              required: '아이디 또는 이메일을 입력해주세요'
            })}
          />
          {errors.usernameOrEmail && <ErrorMessage>{errors.usernameOrEmail.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="password">비밀번호</Label>
          <Input
            id="password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            {...register('password', {
              required: '비밀번호를 입력해주세요',
              minLength: {
                value: 6,
                message: '비밀번호는 최소 6자 이상이어야 합니다'
              }
            })}
          />
          {errors.password && <ErrorMessage>{errors.password.message}</ErrorMessage>}
        </FormGroup>

        {loginError && <ErrorMessage>{loginError}</ErrorMessage>}

        <SubmitButton type="submit" disabled={loading}>
          {loading ? '로그인 중...' : '로그인'}
        </SubmitButton>
      </Form>

      <RegisterLink>
        아직 회원이 아니신가요? <Link to={PATHS.REGISTER}>회원가입</Link>
      </RegisterLink>
    </LoginContainer>
  );
};

export default LoginPage;