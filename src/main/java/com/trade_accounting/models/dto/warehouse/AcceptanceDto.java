package com.trade_accounting.models.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcceptanceDto extends AcceptanceDtoForValidation {

    private Long id;

    private String incomingNumber;

    private String date;

    private Long contractorId;

    private Long warehouseId;

    private Long contractId;

    private Long companyId;

    private Long projectId;

    private String comment;

    private Boolean isSend;

    private Boolean isSent;

    private Boolean isPrint;

    private List<AcceptanceProductionDto> acceptanceProduction;

    private List<Long>acceptanceProductIds;

    private Boolean isRecyclebin;
}
