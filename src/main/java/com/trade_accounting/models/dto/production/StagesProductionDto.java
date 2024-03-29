package com.trade_accounting.models.dto.production;


import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagesProductionDto {

    private Long id;

    private String name;

    private String description;

    private Long departmentId;

    private Long employeeId;

    private DepartmentDto departmentDto;

    private EmployeeDto employeeDto;

}

// Этапы