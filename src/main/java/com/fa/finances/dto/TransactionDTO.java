package com.fa.finances.dto;


import com.fa.finances.models.TransactionType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private CategoryDTO category;
}
