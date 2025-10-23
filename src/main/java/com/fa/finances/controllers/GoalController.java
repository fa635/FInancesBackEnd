package com.fa.finances.controllers;

import com.fa.finances.dto.GoalDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.GoalRequest;
import com.fa.finances.responses.ResponseList;
import com.fa.finances.responses.ResponseObject;
import com.fa.finances.services.interfaces.IGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final IGoalService goalService;

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseObject<Long>> create(@RequestBody GoalRequest req, @PathVariable Long userId) {
        ResponseObject<Long> response = new ResponseObject<>();
        try {
            Long id = goalService.create(req, userId);
            response.setRc(true);
            response.setMsg("Goal created successfully");
            response.setDati(id);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> update(@PathVariable Long id, @RequestBody GoalRequest req) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            goalService.update(id, req);
            response.setRc(true);
            response.setMsg("Goal updated successfully");
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
            goalService.delete(id);
            response.setRc(true);
            response.setMsg("Goal deleted successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseList<GoalDTO>> getAll(@PathVariable Long userId) {
        ResponseList<GoalDTO> response = new ResponseList<>();
        try {
            List<GoalDTO> list = goalService.getAll(userId);
            response.setRc(true);
            response.setMsg("Goals retrieved successfully");
            response.setDati(list);
        } catch (Exception e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<GoalDTO>> getById(@PathVariable Long id) {
        ResponseObject<GoalDTO> response = new ResponseObject<>();
        try {
            GoalDTO dto = goalService.getById(id);
            response.setRc(true);
            response.setMsg("Goal retrieved successfully");
            response.setDati(dto);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}

