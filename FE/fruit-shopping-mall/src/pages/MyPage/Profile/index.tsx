// src/pages/MyPage/Profile/index.tsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppSelector, useAppDispatch } from '@/store/hooks';
import { fetchCurrentUser } from '@/store/slices/authSlice';
import { PATHS } from '@/routes/paths';
import styled from 'styled-components';
import API from '@/services/api';

interface ProfileFormData {
    name: string;
    phoneNumber: string;
    address: string;
}

interface PasswordFormData {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

const ProfileContainer = styled.div`
  margin-top: 24px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
`;

const ProfileGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
`;

const Section = styled.div`
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
`;

const SectionTitle = styled.h2`
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
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
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #4caf50;
  }
`;

const ReadOnlyInput = styled(Input)`
  background-color: #f5f5f5;
  cursor: not-allowed;
`;

const SubmitButton = styled.button`
  padding: 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
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

const ErrorMessage = styled.p`
  color: #f44336;
  font-size: 14px;
  margin-top: 4px;
`;

const SuccessMessage = styled.p`
  color: #4caf50;
  font-size: 14px;
  padding: 8px;
  background-color: #e8f5e9;
  border-radius: 4px;
  margin-top: 16px;
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  font-size: 18px;
  color: #666;
`;

const MyProfilePage = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const { user, isAuthenticated, loading } = useAppSelector((state) => state.auth);

    const [profileSubmitting, setProfileSubmitting] = useState(false);
    const [passwordSubmitting, setPasswordSubmitting] = useState(false);
    const [profileSuccess, setProfileSuccess] = useState(false);
    const [passwordSuccess, setPasswordSuccess] = useState(false);

    const {
        register: registerProfile,
        handleSubmit: handleSubmitProfile,
        formState: { errors: profileErrors },
        setValue: setProfileValue
    } = useForm<ProfileFormData>();

    const {
        register: registerPassword,
        handleSubmit: handleSubmitPassword,
        formState: { errors: passwordErrors },
        reset: resetPasswordForm,
        watch: watchPassword
    } = useForm<PasswordFormData>();

    // 비밀번호 확인을 위해 현재 입력값 감시
    const newPassword = watchPassword('newPassword');

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(PATHS.LOGIN);
            return;
        }

        // 사용자 정보 로드
        dispatch(fetchCurrentUser());
    }, [dispatch, isAuthenticated, navigate]);

    // 프로필 폼 초기화
    useEffect(() => {
        if (user) {
            setProfileValue('name', user.name || '');
            setProfileValue('phoneNumber', user.phoneNumber || '');
            setProfileValue('address', user.address || '');
        }
    }, [user, setProfileValue]);

    // 프로필 정보 업데이트
    const onProfileSubmit = async (data: ProfileFormData) => {
        if (!user) return;

        try {
            setProfileSubmitting(true);
            setProfileSuccess(false);

            await API.put(`/members/${user.id}`, data);

            // 사용자 정보 다시 로드
            dispatch(fetchCurrentUser());
            setProfileSuccess(true);

            // 3초 후 성공 메시지 숨김
            setTimeout(() => {
                setProfileSuccess(false);
            }, 3000);
        } catch (error) {
            console.error('프로필 업데이트 중 오류가 발생했습니다:', error);
            alert('프로필 업데이트에 실패했습니다. 다시 시도해주세요.');
        } finally {
            setProfileSubmitting(false);
        }
    };

    // 비밀번호 변경
    const onPasswordSubmit = async (data: PasswordFormData) => {
        if (!user) return;

        try {
            setPasswordSubmitting(true);
            setPasswordSuccess(false);

            await API.put(`/members/${user.id}/password`, {
                currentPassword: data.currentPassword,
                newPassword: data.newPassword
            });

            setPasswordSuccess(true);
            resetPasswordForm();

            // 3초 후 성공 메시지 숨김
            setTimeout(() => {
                setPasswordSuccess(false);
            }, 3000);
        } catch (error) {
            console.error('비밀번호 변경 중 오류가 발생했습니다:', error);
            alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
        } finally {
            setPasswordSubmitting(false);
        }
    };

    if (loading || !user) {
        return <LoadingContainer>사용자 정보를 불러오는 중...</LoadingContainer>;
    }

    return (
        <ProfileContainer>
            <PageTitle>내 정보 관리</PageTitle>

            <ProfileGrid>
                <div>
                    <Section>
                        <SectionTitle>기본 정보</SectionTitle>
                        <Form onSubmit={handleSubmitProfile(onProfileSubmit)}>
                            <FormGroup>
                                <Label htmlFor="username">아이디</Label>
                                <ReadOnlyInput
                                    id="username"
                                    value={user.username}
                                    readOnly
                                />
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="email">이메일</Label>
                                <ReadOnlyInput
                                    id="email"
                                    value={user.email}
                                    readOnly
                                />
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="name">이름 *</Label>
                                <Input
                                    id="name"
                                    {...registerProfile('name', {
                                        required: '이름을 입력해주세요'
                                    })}
                                />
                                {profileErrors.name && (
                                    <ErrorMessage>{profileErrors.name.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="phoneNumber">연락처</Label>
                                <Input
                                    id="phoneNumber"
                                    placeholder="010-0000-0000"
                                    {...registerProfile('phoneNumber', {
                                        pattern: {
                                            value: /^\d{3}-\d{3,4}-\d{4}$/,
                                            message: '올바른 연락처 형식이 아닙니다 (예: 010-1234-5678)'
                                        }
                                    })}
                                />
                                {profileErrors.phoneNumber && (
                                    <ErrorMessage>{profileErrors.phoneNumber.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="address">주소</Label>
                                <Input
                                    id="address"
                                    {...registerProfile('address')}
                                />
                            </FormGroup>

                            <SubmitButton type="submit" disabled={profileSubmitting}>
                                {profileSubmitting ? '저장 중...' : '정보 저장'}
                            </SubmitButton>

                            {profileSuccess && (
                                <SuccessMessage>프로필 정보가 성공적으로 업데이트 되었습니다.</SuccessMessage>
                            )}
                        </Form>
                    </Section>
                </div>

                <div>
                    <Section>
                        <SectionTitle>비밀번호 변경</SectionTitle>
                        <Form onSubmit={handleSubmitPassword(onPasswordSubmit)}>
                            <FormGroup>
                                <Label htmlFor="currentPassword">현재 비밀번호 *</Label>
                                <Input
                                    id="currentPassword"
                                    type="password"
                                    {...registerPassword('currentPassword', {
                                        required: '현재 비밀번호를 입력해주세요'
                                    })}
                                />
                                {passwordErrors.currentPassword && (
                                    <ErrorMessage>{passwordErrors.currentPassword.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="newPassword">새 비밀번호 *</Label>
                                <Input
                                    id="newPassword"
                                    type="password"
                                    {...registerPassword('newPassword', {
                                        required: '새 비밀번호를 입력해주세요',
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
                                {passwordErrors.newPassword && (
                                    <ErrorMessage>{passwordErrors.newPassword.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Label htmlFor="confirmPassword">비밀번호 확인 *</Label>
                                <Input
                                    id="confirmPassword"
                                    type="password"
                                    {...registerPassword('confirmPassword', {
                                        required: '비밀번호 확인을 입력해주세요',
                                        validate: value =>
                                            value === newPassword || '비밀번호가 일치하지 않습니다'
                                    })}
                                />
                                {passwordErrors.confirmPassword && (
                                    <ErrorMessage>{passwordErrors.confirmPassword.message}</ErrorMessage>
                                )}
                            </FormGroup>

                            <SubmitButton type="submit" disabled={passwordSubmitting}>
                                {passwordSubmitting ? '변경 중...' : '비밀번호 변경'}
                            </SubmitButton>

                            {passwordSuccess && (
                                <SuccessMessage>비밀번호가 성공적으로 변경되었습니다.</SuccessMessage>
                            )}
                        </Form>
                    </Section>

                    <Section>
                        <SectionTitle>계정 정보</SectionTitle>
                        <div style={{ marginBottom: '8px' }}>
                            <strong>회원 등급:</strong> {user.roles.includes('ROLE_ADMIN') ? '관리자' : '일반 회원'}
                        </div>
                        <div>
                            <strong>가입일:</strong> {user.joinDate ? new Date(user.joinDate).toLocaleDateString() : '정보 없음'}
                        </div>
                    </Section>
                </div>
            </ProfileGrid>
        </ProfileContainer>
    );
};

export default MyProfilePage;