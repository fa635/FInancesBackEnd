package com.fa.finances.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalRequest {

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal targetAmount;

    private LocalDate deadline;
}

