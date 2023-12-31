package com.in4mation.festibook.repository.main;

import com.in4mation.festibook.dto.main.FestivalImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FestivalImageMapper {
    List<FestivalImageDTO> getTop5FestivalsByViews();

    // 랜덤으로 이달의 행사 가지고 오는 것
    List<FestivalImageDTO> findCurrentMonthFestivals();
}
