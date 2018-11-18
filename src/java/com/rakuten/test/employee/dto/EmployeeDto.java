package com.rakuten.test.employee.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeDto {
    String employeeId;
    String name;
    String department;
    String designation;
    Double salary;
    Date joiningDate;
}
