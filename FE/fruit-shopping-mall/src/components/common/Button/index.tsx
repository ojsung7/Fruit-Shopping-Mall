// src/components/common/Button/index.tsx
import { ButtonHTMLAttributes, ReactNode } from 'react';
import styled, { css } from 'styled-components';

type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'danger' | 'success';
type ButtonSize = 'small' | 'medium' | 'large';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: ButtonVariant;
    size?: ButtonSize;
    fullWidth?: boolean;
    children: ReactNode;
}

const StyledButton = styled.button<{
    variant: ButtonVariant;
    size: ButtonSize;
    fullWidth: boolean;
}>`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
  
  ${({ fullWidth }) => fullWidth && css`
    width: 100%;
  `}
  
  ${({ size }) => {
        switch (size) {
            case 'small':
                return css`
          padding: 8px 16px;
          font-size: 14px;
        `;
            case 'large':
                return css`
          padding: 16px 24px;
          font-size: 18px;
        `;
            default:
                return css`
          padding: 12px 20px;
          font-size: 16px;
        `;
        }
    }}
  
  ${({ variant }) => {
        switch (variant) {
            case 'primary':
                return css`
          background-color: #4caf50;
          color: white;
          &:hover {
            background-color: #388e3c;
          }
        `;
            case 'secondary':
                return css`
          background-color: #f5f5f5;
          color: #333;
          &:hover {
            background-color: #e0e0e0;
          }
        `;
            case 'outline':
                return css`
          background-color: transparent;
          color: #4caf50;
          border: 1px solid #4caf50;
          &:hover {
            background-color: rgba(76, 175, 80, 0.1);
          }
        `;
            case 'danger':
                return css`
          background-color: #f44336;
          color: white;
          &:hover {
            background-color: #d32f2f;
          }
        `;
            case 'success':
                return css`
          background-color: #2196f3;
          color: white;
          &:hover {
            background-color: #1976d2;
          }
        `;
            default:
                return css`
          background-color: #4caf50;
          color: white;
          &:hover {
            background-color: #388e3c;
          }
        `;
        }
    }}
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    &:hover {
      opacity: 0.5;
    }
  }
`;

const Button = ({
    variant = 'primary',
    size = 'medium',
    fullWidth = false,
    children,
    ...props
}: ButtonProps) => {
    return (
        <StyledButton
            variant={variant}
            size={size}
            fullWidth={fullWidth}
            {...props}
        >
            {children}
        </StyledButton>
    );
};

export default Button;