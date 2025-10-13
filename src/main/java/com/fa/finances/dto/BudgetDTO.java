package com.fa.finances.dto;


import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDTO {
    private Long id;
    private String month; // YYYY-MM
    private BigDecimal amount;
    private CategoryDTO category;
}

