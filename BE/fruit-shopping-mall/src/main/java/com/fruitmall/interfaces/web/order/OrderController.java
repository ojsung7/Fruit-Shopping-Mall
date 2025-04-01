package com.fruitmall.interfaces.web.order;

import com.fruitmall.domain.order.application.OrderService;
import com.fruitmall.domain.order.application.dto.CreateOrderRequestDto;
import com.fruitmall.domain.order.application.dto.OrderDto;
import com.fruitmall.domain.order.application.dto.UpdateOrderStatusRequestDto;
import com.fruitmall.domain.order.domain.OrderStatus;
import com.fruitmall.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "주문 관리", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        OrderDto orderDto = orderService.createOrder(memberId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @Operation(summary = "주문 조회", description = "주문 ID로 주문을 조회합니다")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrder(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long id) {
        OrderDto orderDto = orderService.findById(id);
        return ResponseEntity.ok(orderDto);
    }

    @Operation(summary = "내 주문 목록 조회", description = "현재 로그인한 회원의 주문 목록을 조회합니다")
    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getMyOrders() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        List<OrderDto> orders = orderService.findByMember(memberId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "회원별 주문 목록 조회", description = "특정 회원의 주문 목록을 조회합니다 (관리자 전용)")
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByMember(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long memberId) {
        List<OrderDto> orders = orderService.findByMember(memberId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "주문 상태별 조회", description = "주문 상태로 주문을 조회합니다 (관리자 전용)")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(
            @Parameter(description = "주문 상태", required = true)
            @PathVariable OrderStatus status) {
        List<OrderDto> orders = orderService.findByOrderStatus(status);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "기간별 주문 조회", description = "특정 기간 내의 주문을 조회합니다 (관리자 전용)")
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OrderDto> orders = orderService.findByOrderDateBetween(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "주문 상태 변경", description = "주문 상태를 변경합니다 (관리자는 모든 상태, 일반 회원은 취소만 가능)")
    @PutMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDto dto) {
        OrderDto updatedOrder = orderService.updateOrderStatus(id, dto);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "주문 취소", description = "주문을 취소합니다")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> cancelOrder(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long id) {
        UpdateOrderStatusRequestDto dto = new UpdateOrderStatusRequestDto(OrderStatus.CANCELLED);
        OrderDto cancelledOrder = orderService.updateOrderStatus(id, dto);
        return ResponseEntity.ok(cancelledOrder);
    }

    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다 (관리자 전용)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}