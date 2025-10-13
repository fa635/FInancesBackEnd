package com.fa.finances.requests;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequest {

    @NotNull
    private String month;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long categoryId;
}
