// 금액 포맷팅 (1000 -> 1,000원)
export const formatPrice = (price: number): string => {
    return price.toLocaleString('ko-KR') + '원';
};
  
// 날짜 포맷팅 (YYYY-MM-DD)
export const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).replace(/\. /g, '-').replace(/\.$/, '');
};

// 별점 계산 (소수점 한자리까지)
export const formatRating = (rating: number): number => {
    return Math.round(rating * 10) / 10;
};

// 텍스트 줄임 처리
export const truncateText = (text: string, maxLength: number): string => {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
};