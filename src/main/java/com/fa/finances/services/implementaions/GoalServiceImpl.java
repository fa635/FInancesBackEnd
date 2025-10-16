package com.fa.finances.services.implementaions;


import com.fa.finances.dto.GoalDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Goal;
import com.fa.finances.models.User;
import com.fa.finances.repositories.GoalRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.GoalRequest;
import com.fa.finances.services.interfaces.IGoalService;
import com.fa.finances.utils.GoalMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements IGoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public Long create(GoalRequest req, Long userId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));

        Goal goal = Goal.builder()
                .name(req.getName())
                .targetAmount(req.getTargetAmount())
                .currentAmount(req.getCurrentAmount())
                .deadline(req.getDeadline())
                .user(user)
                .build();

        goalRepository.save(goal);
        return goal.getId();
    }

    @Override
    public void update(Long id, GoalRequest req) throws FinancesException {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Goal not found"));

        goal.setName(req.getName());
        goal.setTargetAmount(req.getTargetAmount());
        goal.setCurrentAmount(req.getCurrentAmount());
        goal.setDeadline(req.getDeadline());

        goalRepository.save(goal);
    }

    @Override
    public void delete(Long id) throws FinancesException {
        if (!goalRepository.existsById(id)) {
            throw new FinancesException("Goal not found");
        }
        goalRepository.deleteById(id);
    }

    @Override
    public List<GoalDTO> getAll(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return goalRepository.findByUser(user).stream()
                .map(GoalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GoalDTO getById(Long id) throws FinancesException {
    	return GoalMapper.toDTO(goalRepository.findById(id)
    			.orElseThrow(() -> new FinancesException("Goal not found")));
    }
}
