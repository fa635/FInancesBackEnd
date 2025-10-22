package com.fa.finances.controllers;

import com.fa.finances.dto.UserDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.UserRequest;
import com.fa.finances.responses.ResponseList;
import com.fa.finances.responses.ResponseObject;
import com.fa.finances.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject<Long>> createUser(@RequestBody UserRequest req) {
        ResponseObject<Long> response = new ResponseObject<>();
        try {
            Long id = userService.create(req);
            response.setRc(true);
            response.setMsg("User created successfully");
            response.setDati(id);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/signin")
    public ResponseObject<String> signin(@RequestBody UserRequest req) {
        ResponseObject<String> response = new ResponseObject<>();
        try {
            String token = userService.signin(req);
            response.setRc(true);
            response.setMsg("Login successful");
            response.setDati(token);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> updateUser(@PathVariable Long id, @RequestBody UserRequest req) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            userService.update(id, req);
            response.setRc(true);
            response.setMsg("User updated successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteUser(@PathVariable Long id) {
        ResponseObject<Void> response = new ResponseObject<>();
        try {
            userService.delete(id);
            response.setRc(true);
            response.setMsg("User deleted successfully");
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseList<UserDTO>> getAllUsers() {
        ResponseList<UserDTO> response = new ResponseList<>();
        try {
            List<UserDTO> users = userService.getAll();
            response.setRc(true);
            response.setMsg("Users retrieved successfully");
            response.setDati(users);
        } catch (Exception e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<UserDTO>> getUserById(@PathVariable Long id) {
        ResponseObject<UserDTO> response = new ResponseObject<>();
        try {
            UserDTO user = userService.getById(id);
            response.setRc(true);
            response.setMsg("User retrieved successfully");
            response.setDati(user);
        } catch (FinancesException e) {
            response.setRc(false);
            response.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
