package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDto;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDtoId;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsMapper {
    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    StatisticsDto toDto(Statistics statistics){
        return new StatisticsDto(statistics.getId(),
                userMapper.toDto(statistics.getUser()),
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned());
    }

    Statistics toEntity(StatisticsDtoId statisticsDtoId){
        Statistics statistics = new Statistics();
        statistics.setUser(userService.getUser(statisticsDtoId.getUserId()).get());
        statistics.setTotalTrainings(statisticsDtoId.getTotalTrainings());
        statistics.setTotalDistance(statisticsDtoId.getTotalDistance());
        statistics.setTotalCaloriesBurned(statisticsDtoId.getTotalCaloriesBurned());
        return statistics;
    }
}
