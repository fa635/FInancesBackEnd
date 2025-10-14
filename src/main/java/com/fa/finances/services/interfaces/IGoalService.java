package com.fa.finances.services.interfaces;


import com.fa.finances.dto.GoalDTO;
import com.fa.finances.requests.GoalRequest;
import com.fa.finances.exception.FinancesException;

import java.util.List;

public interface IGoalService {

    Long create(GoalRequest req, Long userId) throws FinancesException;

    void update(Long id, GoalRequest req) throws FinancesException;

    void delete(Long id) throws FinancesException;

    List<GoalDTO> getAll(Long userId);

    GoalDTO getById(Long id) throws FinancesException;
}

