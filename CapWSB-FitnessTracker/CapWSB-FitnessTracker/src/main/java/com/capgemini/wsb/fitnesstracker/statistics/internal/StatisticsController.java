package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDto;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDtoId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsServiceImpl statisticsServiceImpl;
    private final StatisticsMapper statisticsMapper;
    private final StatisticsRepository statisticsRepository;

    @PostMapping()
    @ResponseStatus(CREATED)
    public StatisticsDto addStatistics(@RequestBody StatisticsDtoId statisticsDtoId){
        return statisticsMapper.toDto(statisticsServiceImpl.createStatisticsEntity(statisticsDtoId));
    }

    @PutMapping("/{statisticsId}")
    public StatisticsDto updateStatistics(@PathVariable Long statisticsId, @RequestBody StatisticsDtoId statisticsDtoId){
        Statistics statistics = statisticsMapper.toEntity(statisticsDtoId);
        statistics.setId(statisticsId);
        final Statistics savedStatistics = statisticsRepository.save(statistics);
        return statisticsMapper.toDto(savedStatistics);
    }

    @GetMapping("/{userId}")
    public List<StatisticsDto> getStatisticsForUser(@PathVariable("userId") Long userId){
        return statisticsServiceImpl.getUserStatistics(userId).stream().map(statisticsMapper::toDto).collect(Collectors.toList());
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteById(@RequestParam("statisticsId") Long statisticsId){
        statisticsServiceImpl.deleteById(statisticsId);
        return ResponseEntity.ok(statisticsId);
    }

    @GetMapping("/calories/{caloriesBurned}")
    public List<StatisticsDto> getStatisticsByCaloriesBurned(@PathVariable("caloriesBurned") double caloriesBurned){
        return statisticsRepository.findByTotalCaloriesBurnedGreaterThan(caloriesBurned).stream().map(statisticsMapper::toDto).collect(Collectors.toList());
    }

}
