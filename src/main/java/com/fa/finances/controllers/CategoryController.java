package com.fa.finances.controllers;


import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.CategoryRequest;
import com.fa.finances.responses.ResponseList;
import com.fa.finances.responses.ResponseObject;
import com.fa.finances.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseObject<Long>> create(@RequestBody CategoryRequest req, @PathVariable Long userId) {
        ResponseObject<Long> response = new ResponseObject<>();
        try {
            Long id = categoryService.create(req, userId);
            response.setRc(true);
            response.setMsg("Category created successfully");
            response.setDati(id);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> update(@PathVariable Long id, @RequestBody CategoryRequest req) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            categoryService.update(id, req);
            response.setRc(true);
            response.setMsg("Category updated successfully");
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
            categoryService.delete(id);
            response.setRc(true);
            response.setMsg("Category deleted successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseList<CategoryDTO>> getAllByUser(@PathVariable Long userId) {
        ResponseList<CategoryDTO> response = new ResponseList<>();
        try {
            List<CategoryDTO> list = categoryService.getAll(userId);
            response.setRc(true);
            response.setMsg("Categories retrieved successfully");
            response.setDati(list);
        } catch (Exception e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CategoryDTO>> getById(@PathVariable Long id) {
        ResponseObject<CategoryDTO> response = new ResponseObject<>();
        try {
            CategoryDTO dto = categoryService.getById(id);
            response.setRc(true);
            response.setMsg("Category retrieved successfully");
            response.setDati(dto);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
