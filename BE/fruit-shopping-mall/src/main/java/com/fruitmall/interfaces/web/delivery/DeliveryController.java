package com.fruitmall.interfaces.web.delivery;

import com.fruitmall.domain.delivery.application.DeliveryService;
import com.fruitmall.domain.delivery.application.dto.*;
import com.fruitmall.domain.delivery.domain.DeliveryStatus;
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
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@Tag(name = "배송 관리", description = "배송 관련 API")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송 정보 생성", description = "주문에 대한 배송 정보를 생성합니다 (관리자 전용)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDto> createDelivery(@Valid @RequestBody CreateDeliveryRequestDto dto) {
        DeliveryDto deliveryDto = deliveryService.createDelivery(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryDto);
    }

    @Operation(summary = "배송 정보 조회", description = "배송 ID로 배송 정보를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDelivery(
            @Parameter(description = "배송 ID", required = true)
            @PathVariable Long id) {
        DeliveryDto deliveryDto = deliveryService.findById(id);
        return ResponseEntity.ok(deliveryDto);
    }

    @Operation(summary = "주문별 배송 정보 조회", description = "주문 ID로 배송 정보를 조회합니다")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDto> getDeliveryByOrder(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long orderId) {
        DeliveryDto deliveryDto = deliveryService.findByOrderId(orderId);
        return ResponseEntity.ok(deliveryDto);
    }

    @Operation(summary = "모든 배송 정보 조회", description = "모든 배송 정보를 조회합니다 (관리자 전용)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        List<DeliveryDto> deliveries = deliveryService.findAll();
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "배송 상태별 조회", description = "배송 상태로 배송 정보를 조회합니다 (관리자 전용)")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryDto>> getDeliveriesByStatus(
            @Parameter(description = "배송 상태", required = true)
            @PathVariable DeliveryStatus status) {
        List<DeliveryDto> deliveries = deliveryService.findByStatus(status);
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "예상 배송일별 조회", description = "예상 배송일로 배송 정보를 조회합니다 (관리자 전용)")
    @GetMapping("/expected-date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryDto>> getDeliveriesByExpectedDate(
            @Parameter(description = "예상 배송일 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DeliveryDto> deliveries = deliveryService.findByExpectedDeliveryDate(date);
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "기간별 예상 배송 조회", description = "예상 배송일 기간으로 배송 정보를 조회합니다 (관리자 전용)")
    @GetMapping("/expected-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryDto>> getDeliveriesByExpectedDateRange(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DeliveryDto> deliveries = deliveryService.findByExpectedDeliveryDateBetween(fromDate, toDate);
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "운송장 번호로 조회", description = "운송장 번호로 배송 정보를 조회합니다")
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<DeliveryDto> getDeliveryByTrackingNumber(
            @Parameter(description = "운송장 번호", required = true)
            @PathVariable String trackingNumber) {
        DeliveryDto deliveryDto = deliveryService.findByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(deliveryDto);
    }

    @Operation(summary = "배송 상태 변경", description = "배송 상태를 변경합니다 (관리자 전용)")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDto> updateDeliveryStatus(
            @Parameter(description = "배송 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryStatusRequestDto dto) {
        DeliveryDto updatedDelivery = deliveryService.updateDeliveryStatus(id, dto);
        return ResponseEntity.ok(updatedDelivery);
    }

    @Operation(summary = "배송 정보 변경", description = "배송 정보를 변경합니다 (관리자 전용)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDto> updateDelivery(
            @Parameter(description = "배송 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryRequestDto dto) {
        DeliveryDto updatedDelivery = deliveryService.updateDelivery(id, dto);
        return ResponseEntity.ok(updatedDelivery);
    }

    @Operation(summary = "운송장 정보 변경", description = "운송장 정보를 변경합니다 (관리자 전용)")
    @PutMapping("/{id}/tracking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDto> updateTrackingInfo(
            @Parameter(description = "배송 ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "택배사", required = true)
            @RequestParam String courier,
            @Parameter(description = "운송장 번호", required = true)
            @RequestParam String trackingNumber) {
        DeliveryDto updatedDelivery = deliveryService.updateTrackingInfo(id, courier, trackingNumber);
        return ResponseEntity.ok(updatedDelivery);
    }

    @Operation(summary = "배송 취소", description = "배송을 취소합니다 (관리자 전용)")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDto> cancelDelivery(
            @Parameter(description = "배송 ID", required = true)
            @PathVariable Long id) {
        DeliveryDto cancelledDelivery = deliveryService.cancelDelivery(id);
        return ResponseEntity.ok(cancelledDelivery);
    }
}