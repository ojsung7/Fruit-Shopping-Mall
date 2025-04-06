// src/pages/Auth/Register/index.tsx
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppSelector } from '@/store/hooks';
import { PATHS } from '@/routes/paths';
import { register as registerUser } from '@/services/authService';
import styled from 'styled-components';

interface RegisterFormData {
    username: string;
    email: string;
    password: string;
    passwordConfirm: string;
    name: string;
    phoneNumber: string;
    birthDate?: string;
    address?: string;
}

const RegisterContainer = styled.div`
  max-width: 500px;
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

const LoginLink = styled.div`
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

const RequiredMark = styled.span`
  color: #f44336;
  margin-left: 4px;
`;

const RegisterPage = () => {
    const { register, handleSubmit, watch, formState: { errors } } = useForm<RegisterFormData>();
    const [registering, setRegistering] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();
    const { isAuthenticated } = useAppSelector((state) => state.auth);

    // 비밀번호 확인을 위해 현재 입력된 비밀번호 값 감시
    const password = watch('password');

    useEffect(() => {
        // 이미 로그인한 경우 홈으로 리다이렉트
        if (isAuthenticated) {
            navigate(PATHS.HOME);
        }
    }, [isAuthenticated, navigate]);

    const onSubmit = async (data: RegisterFormData) => {
        try {
            setRegistering(true);
            setError(null);

            // 비밀번호 확인 일치 여부 검증
            if (data.password !== data.passwordConfirm) {
                setError('비밀번호가 일치하지 않습니다.');
                setRegistering(false);
                return;
            }

            const { passwordConfirm, ...registerData } = data;

            await registerUser(registerData);

            alert('회원가입이 완료되었습니다. 로그인해주세요.');
            navigate(PATHS.LOGIN);
        } catch (err: any) {
            console.error('회원가입 중 오류가 발생했습니다:', err);
            setError(err.response?.data?.message || '회원가입에 실패했습니다. 다시 시도해주세요.');
        } finally {
            setRegistering(false);
        }
    };

    return (
        <RegisterContainer>
            <Title>회원가입</Title>

            <Form onSubmit={handleSubmit(onSubmit)}>
                <FormGroup>
                    <Label htmlFor="username">
                        아이디<RequiredMark>*</RequiredMark>
                    </Label>
                    <Input
                        id="username"
                        type="text"
                        placeholder="영문, 숫자, 언더스코어(_)만 사용 가능"
                        {...register('username', {
                            required: '아이디를 입력해주세요',
                            minLength: {
                                value: 4,
                                message: '아이디는 최소 4자 이상이어야 합니다'
                            },
                            maxLength: {
                                value: 20,
                                message: '아이디는 최대 20자까지 가능합니다'
                            },
                            pattern: {
                                value: /^[a-zA-Z0-9_]*$/,
                                message: '아이디는 영문, 숫자, 언더스코어(_)만 사용 가능합니다'
                            }
                        })}
                    />
                    {errors.username && <ErrorMessage>{errors.username.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="email">
                        이메일<RequiredMark>*</RequiredMark>
                    </Label>
                    <Input
                        id="email"
                        type="email"
                        placeholder="example@email.com"
                        {...register('email', {
                            required: '이메일을 입력해주세요',
                            pattern: {
                                value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                                message: '올바른 이메일 형식이 아닙니다'
                            }
                        })}
                    />
                    {errors.email && <ErrorMessage>{errors.email.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="password">
                        비밀번호<RequiredMark>*</RequiredMark>
                    </Label>
                    <Input
                        id="password"
                        type="password"
                        placeholder="영문, 숫자, 특수문자 조합 8자 이상"
                        {...register('password', {
                            required: '비밀번호를 입력해주세요',
                            minLength: {
                                value: 8,
                                message: '비밀번호는 최소 8자 이상이어야 합니다'
                            },
                            pattern: {
                                value: /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/,
                                message: '비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다'
                            }
                        })}
                    />
                    {errors.password && <ErrorMessage>{errors.password.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="passwordConfirm">
                        비밀번호 확인<RequiredMark>*</RequiredMark>
                    </Label>
                    <Input
                        id="passwordConfirm"
                        type="password"
                        placeholder="비밀번호 재입력"
                        {...register('passwordConfirm', {
                            required: '비밀번호 확인을 입력해주세요',
                            validate: value =>
                                value === password || '비밀번호가 일치하지 않습니다'
                        })}
                    />
                    {errors.passwordConfirm && <ErrorMessage>{errors.passwordConfirm.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="name">
                        이름<RequiredMark>*</RequiredMark>
                    </Label>
                    <Input
                        id="name"
                        type="text"
                        {...register('name', {
                            required: '이름을 입력해주세요'
                        })}
                    />
                    {errors.name && <ErrorMessage>{errors.name.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="phoneNumber">
                        연락처
                    </Label>
                    <Input
                        id="phoneNumber"
                        type="text"
                        placeholder="010-0000-0000"
                        {...register('phoneNumber', {
                            pattern: {
                                value: /^\d{3}-\d{3,4}-\d{4}$/,
                                message: '올바른 연락처 형식이 아닙니다 (예: 010-1234-5678)'
                            }
                        })}
                    />
                    {errors.phoneNumber && <ErrorMessage>{errors.phoneNumber.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="birthDate">
                        생년월일
                    </Label>
                    <Input
                        id="birthDate"
                        type="date"
                        {...register('birthDate')}
                    />
                    {errors.birthDate && <ErrorMessage>{errors.birthDate.message}</ErrorMessage>}
                </FormGroup>

                <FormGroup>
                    <Label htmlFor="address">
                        주소
                    </Label>
                    <Input
                        id="address"
                        type="text"
                        {...register('address')}
                    />
                    {errors.address && <ErrorMessage>{errors.address.message}</ErrorMessage>}
                </FormGroup>

                {error && <ErrorMessage>{error}</ErrorMessage>}

                <SubmitButton type="submit" disabled={registering}>
                    {registering ? '가입 중...' : '회원가입'}
                </SubmitButton>
            </Form>

            <LoginLink>
                이미 계정이 있으신가요? <Link to={PATHS.LOGIN}>로그인</Link>
            </LoginLink>
        </RegisterContainer>
    );
};

export default RegisterPage;