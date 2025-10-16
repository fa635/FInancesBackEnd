package com.fa.finances.utils;


import com.fa.finances.dto.GoalDTO;
import com.fa.finances.models.Goal;

public class GoalMapper {

    public static GoalDTO toDTO(Goal g) {
        if (g == null) return null;
        return new GoalDTO(g.getId(), g.getName(), g.getTargetAmount(), g.getCurrentAmount(), g.getDeadline());
    }
}
