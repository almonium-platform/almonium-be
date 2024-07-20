package com.almonium.infra.payment.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String status;
}
