package com.example.DateCock.Service;

import com.example.DateCock.DTO.BusinessDTO;
import com.example.DateCock.DTO.DateCourseDTO;

import java.util.List;

public interface DateCourseService {
    List<BusinessDTO> conditionselect(String step1, String step2, String step3);

    List<BusinessDTO> conditionselect(int step1, String step2, String step3);

    List<DateCourseDTO> getFilteredCourses(String age, String region, String type);
}
