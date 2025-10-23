package com.fa.finances.controllers;


import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.TransactionRequest;
import com.fa.finances.responses.ResponseList;
import com.fa.finances.responses.ResponseObject;
import com.fa.finances.services.interfaces.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseObject<Long>> create(@RequestBody TransactionRequest req, @PathVariable Long userId) {
        ResponseObject<Long> response = new ResponseObject<>();
        try {
            Long id = transactionService.create(req, userId);
            response.setRc(true);
            response.setMsg("Transaction created successfully");
            response.setDati(id);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> update(@PathVariable Long id, @RequestBody TransactionRequest req) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            transactionService.update(id, req);
            response.setRc(true);
            response.setMsg("Transaction updated successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> delete(@PathVariable Long id) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            transactionService.delete(id);
            response.setRc(true);
            response.setMsg("Transaction deleted successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseList<TransactionDTO>> getAll(@PathVariable Long userId) {
        ResponseList<TransactionDTO> response = new ResponseList<>();
        try {
            List<TransactionDTO> list = transactionService.getAll(userId);
            response.setRc(true);
            response.setMsg("Transactions retrieved successfully");
            response.setDati(list);
        } catch (Exception e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<TransactionDTO>> getById(@PathVariable Long id) {
        ResponseObject<TransactionDTO> response = new ResponseObject<>();
        try {
            TransactionDTO dto = transactionService.getById(id);
            response.setRc(true);
            response.setMsg("Transaction retrieved successfully");
            response.setDati(dto);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/date")
    public ResponseEntity<ResponseList<TransactionDTO>> getByDateRange(
            @RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        ResponseList<TransactionDTO> response = new ResponseList<>();
        try {
            List<TransactionDTO> list = transactionService.getByDateRange(userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
            response.setRc(true);
            response.setMsg("Transactions retrieved successfully");
            response.setDati(list);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/category")
    public ResponseEntity<ResponseList<TransactionDTO>> getByCategory(
            @RequestParam Long userId,
            @RequestParam Long categoryId) {
        ResponseList<TransactionDTO> response = new ResponseList<>();
        try {
            List<TransactionDTO> list = transactionService.getByCategory(userId, categoryId);
            response.setRc(true);
            response.setMsg("Transactions retrieved successfully");
            response.setDati(list);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
