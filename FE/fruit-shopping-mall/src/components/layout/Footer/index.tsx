// src/components/layout/Footer/index.tsx
import styled from 'styled-components';
import { Link } from 'react-router-dom';

const FooterContainer = styled.footer`
  background-color: #333;
  color: #fff;
  padding: 40px 0;
`;

const FooterContent = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
`;

const FooterSection = styled.div`
  h3 {
    font-size: 18px;
    margin-bottom: 16px;
    font-weight: 600;
  }
`;

const FooterLink = styled(Link)`
  display: block;
  color: #ccc;
  margin-bottom: 8px;
  &:hover {
    color: #fff;
  }
`;

const ContactItem = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #ccc;
`;

const Copyright = styled.div`
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #aaa;
`;

const Footer = () => {
    return (
        <FooterContainer>
            <div className="container">
                <FooterContent>
                    <FooterSection>
                        <h3>고객센터</h3>
                        <ContactItem>전화: 1588-0000</ContactItem>
                        <ContactItem>이메일: support@fruitmall.com</ContactItem>
                        <ContactItem>운영시간: 평일 09:00 - 18:00</ContactItem>
                    </FooterSection>

                    <FooterSection>
                        <h3>쇼핑 정보</h3>
                        <FooterLink to="#">신상품</FooterLink>
                        <FooterLink to="#">베스트 상품</FooterLink>
                        <FooterLink to="#">특가 상품</FooterLink>
                        <FooterLink to="#">제철 과일</FooterLink>
                    </FooterSection>

                    <FooterSection>
                        <h3>고객 지원</h3>
                        <FooterLink to="#">자주 묻는 질문</FooterLink>
                        <FooterLink to="#">배송 안내</FooterLink>
                        <FooterLink to="#">교환/반품 안내</FooterLink>
                        <FooterLink to="#">개인정보 처리방침</FooterLink>
                    </FooterSection>

                    <FooterSection>
                        <h3>회사 정보</h3>
                        <ContactItem>회사명: 과일마켓</ContactItem>
                        <ContactItem>대표: 홍길동</ContactItem>
                        <ContactItem>사업자등록번호: 123-45-67890</ContactItem>
                        <ContactItem>주소: 서울시 강남구 과일로 123</ContactItem>
                    </FooterSection>
                </FooterContent>

                <Copyright>
                    © {new Date().getFullYear()} 과일마켓. All rights reserved.
                </Copyright>
            </div>
        </FooterContainer>
    );
};

export default Footer;