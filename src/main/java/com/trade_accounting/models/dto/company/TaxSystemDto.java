package com.trade_accounting.models.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxSystemDto {

    private Long id;

    private String name;

    private String sortNumber;

}
