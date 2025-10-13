package com.fa.finances.requests;


import com.fa.finances.models.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    private String description;

    @NotNull
    private Long categoryId;
}
