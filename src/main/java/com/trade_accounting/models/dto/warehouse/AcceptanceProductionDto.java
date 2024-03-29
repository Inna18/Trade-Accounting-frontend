package com.trade_accounting.models.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceProductionDto {

    private Long id;

    private BigDecimal amount;

    private Long acceptanceId;

    private Long productId;

    private BigDecimal price;
}
