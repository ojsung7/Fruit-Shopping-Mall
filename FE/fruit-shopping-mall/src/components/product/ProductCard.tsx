import React from 'react';
import { Link } from 'react-router-dom';
import './ProductCard.css';
import { Product as StoreProduct } from '@/store/slices/productSlice';

// 컴포넌트 props 타입 정의
interface ProductCardProps {
    product: StoreProduct;
    onAddToCart?: (id: number) => void;
}

const ProductCard: React.FC<ProductCardProps> = ({
    product,
    onAddToCart,
}) => {
    const {
        id,
        fruitName,
        price,
        imageUrl,
        description,
    } = product;

    // API에서 discount 필드가 없으므로 별도 변수로 처리
    const discount = 0; // 할인이 필요하면 여기서 계산 로직 구현

    const handleAddToCart = () => {
        if (onAddToCart) {
            onAddToCart(id);
        }
    };

    // 할인 여부 확인 (현재는 할인 기능이 없으므로 false)
    const isDiscounted = false;
    const discountedPrice = price;

    return (
        <div className="product-card">
            <Link to={`/products/${id}`} className="product-link">
                <div className="product-image-container">
                    <img src={imageUrl} alt={fruitName} className="product-image" />
                    {isDiscounted && (
                        <span className="discount-badge">할인</span>
                    )}
                </div>
                <div className="product-info">
                    <h3 className="product-name">{fruitName}</h3>
                    <div className="product-price">
                        {isDiscounted ? (
                            <>
                                <span className="original-price">{price.toLocaleString()}원</span>
                                <span className="discounted-price">{discountedPrice.toLocaleString()}원</span>
                            </>
                        ) : (
                            <span>{price.toLocaleString()}원</span>
                        )}
                    </div>
                    {description && <p className="product-description">{description}</p>}
                </div>
            </Link>
            <button
                className="add-to-cart-button"
                onClick={handleAddToCart}
            >
                장바구니에 추가
            </button>
        </div>
    );
};

export default ProductCard;