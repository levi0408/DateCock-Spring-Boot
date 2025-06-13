package com.example.DateCock.Service;

import com.example.DateCock.DTO.BusinessDTO;
import com.example.DateCock.DTO.DateCourseDTO;
import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DateCourseServiceImp implements DateCourseService{

    @Autowired
    BusinessRepository businessRepository;

    @Override
    public List<BusinessDTO> conditionselect(String step1, String step2, String step3) {
        return List.of();
    }

    @Override
    public List<BusinessDTO> conditionselect(int step1, String step2, String step3) {
        return List.of();
    }

    @Override
    public List<DateCourseDTO> getFilteredCourses(String step1, String step2, String step3) {
        int age = Integer.parseInt(step1);
        List<BusinessEntity> entityList = businessRepository.findByCondition(age, step2, step3);

        return entityList.stream()
                .map(entity -> {
                    DateCourseDTO dto = new DateCourseDTO();
                    dto.setBusinessname(entity.getBusinessname());
                    dto.setAddress(entity.getAddress());
                    dto.setImage(entity.getImage());
                    dto.setBusinesstime(entity.getBusinesstime());
                    dto.setPhone(entity.getPhone());
                    dto.setInformation(entity.getInformation());
                    return dto;
                })
                .collect(Collectors.toList());

    }


}
