package com.fa.finances.services.interfaces;

import com.fa.finances.dto.UserDTO;
import com.fa.finances.requests.UserRequest;
import com.fa.finances.exception.FinancesException;

import java.util.List;

public interface IUserService {

    Long create(UserRequest req) throws FinancesException;

    void update(Long id, UserRequest req) throws FinancesException;

    void delete(Long id) throws FinancesException;

    List<UserDTO> getAll();

    UserDTO getById(Long id) throws FinancesException;

    UserDTO getByEmail(String email) throws FinancesException;
    
    String signin(UserRequest req) throws FinancesException;
}

