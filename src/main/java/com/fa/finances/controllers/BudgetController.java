package com.fa.finances.controllers;


import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.BudgetRequest;
import com.fa.finances.responses.ResponseList;
import com.fa.finances.responses.ResponseObject;
import com.fa.finances.services.interfaces.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final IBudgetService budgetService;

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseObject<Long>> create(@RequestBody BudgetRequest req, @PathVariable Long userId) {
        ResponseObject<Long> response = new ResponseObject<>();
        try {
            Long id = budgetService.create(req, userId);
            response.setRc(true);
            response.setMsg("Budget created successfully");
            response.setDati(id);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> update(@PathVariable Long id, @RequestBody BudgetRequest req) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            budgetService.update(id, req);
            response.setRc(true);
            response.setMsg("Budget updated successfully");
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
            budgetService.delete(id);
            response.setRc(true);
            response.setMsg("Budget deleted successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseList<BudgetDTO>> getAll(@PathVariable Long userId) {
        ResponseList<BudgetDTO> response = new ResponseList<>();
        try {
            List<BudgetDTO> list = budgetService.getAll(userId);
            response.setRc(true);
            response.setMsg("Budgets retrieved successfully");
            response.setDati(list);
        } catch (Exception e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<BudgetDTO>> getById(@PathVariable Long id) {
        ResponseObject<BudgetDTO> response = new ResponseObject<>();
        try {
            BudgetDTO dto = budgetService.getById(id);
            response.setRc(true);
            response.setMsg("Budget retrieved successfully");
            response.setDati(dto);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseObject<BudgetDTO>> getByMonthAndCategory(
            @RequestParam Long userId,
            @RequestParam String month,
            @RequestParam Long categoryId) {
        ResponseObject<BudgetDTO> response = new ResponseObject<>();
        try {
            BudgetDTO dto = budgetService.getByMonthAndCategory(userId, month, categoryId);
            response.setRc(true);
            response.setMsg("Budget retrieved successfully");
            response.setDati(dto);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
