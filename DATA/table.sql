-- 회원 테이블
CREATE TABLE MEMBER (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    birth_date DATE,
    address VARCHAR(255),
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    member_grade VARCHAR(20) DEFAULT 'BRONZE'
);

-- 과일 테이블
CREATE TABLE FRUIT (
    fruit_id INT AUTO_INCREMENT PRIMARY KEY,
    fruit_name VARCHAR(100) NOT NULL,
    origin VARCHAR(100),
    stock_quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50),
    season VARCHAR(50),
    description TEXT,
    image_url VARCHAR(255)
);

-- 주문 테이블
CREATE TABLE `ORDER` (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    order_status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- 주문 상세 테이블
CREATE TABLE ORDER_DETAIL (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    fruit_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `ORDER`(order_id),
    FOREIGN KEY (fruit_id) REFERENCES FRUIT(fruit_id)
);

-- 배송 테이블
CREATE TABLE DELIVERY (
    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    delivery_status VARCHAR(50) DEFAULT 'PROCESSING',
    expected_delivery_date DATE,
    actual_delivery_date DATE,
    courier VARCHAR(100),
    tracking_number VARCHAR(100),
    FOREIGN KEY (order_id) REFERENCES `ORDER`(order_id)
);

-- 리뷰 테이블
CREATE TABLE REVIEW (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    order_detail_id INT NOT NULL,
    member_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(255),
    FOREIGN KEY (order_detail_id) REFERENCES ORDER_DETAIL(order_detail_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- 장바구니 테이블
CREATE TABLE CART (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    fruit_id INT NOT NULL,
    quantity INT NOT NULL,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (fruit_id) REFERENCES FRUIT(fruit_id)
);

-- 찜 목록 테이블
CREATE TABLE WISHLIST (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    fruit_id INT NOT NULL,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (fruit_id) REFERENCES FRUIT(fruit_id)
);

-- 결제 테이블
CREATE TABLE PAYMENT (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_type VARCHAR(50),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (order_id) REFERENCES `ORDER`(order_id)
);

-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_member_email ON MEMBER(email);
CREATE INDEX idx_fruit_name ON FRUIT(fruit_name);
CREATE INDEX idx_order_member ON `ORDER`(member_id);
CREATE INDEX idx_order_date ON `ORDER`(order_date);
CREATE INDEX idx_order_detail_order ON ORDER_DETAIL(order_id);
CREATE INDEX idx_delivery_order ON DELIVERY(order_id);
CREATE INDEX idx_review_member ON REVIEW(member_id);