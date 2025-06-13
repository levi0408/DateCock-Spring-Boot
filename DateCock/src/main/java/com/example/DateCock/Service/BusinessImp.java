package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BusinessImp implements BusinessService{
    @Autowired
    BusinessRepository businessRepository;

    @Override
    public void insertbusiness(BusinessEntity businessEntity) {
        businessRepository.save(businessEntity);
    }

    @Override
    public BusinessEntity allout(String businessnumber) {
        return businessRepository.findById(businessnumber).orElse(null);
    }

    @Override
    public BusinessEntity updatego(String businessnumber) {
        return businessRepository.findById(businessnumber).orElse(null);
    }

    @Override
    public void updateall(BusinessEntity businessEntity) {
        businessRepository.save(businessEntity);
    }

    @Override
    public void deleteall(String businessnumber) {
        businessRepository.deleteById(businessnumber);
    }

    @Override
    public List<BusinessEntity> allselect() {
        return businessRepository.findAll();
    }

    @Override
    public BusinessEntity datereservationOut(String businessname) {
        return businessRepository.datereservationOut(businessname);
    }

    @Override
    public String businessAddressOut(String businessname) {
        return businessRepository.businessAddressOut(businessname);
    }

    @Override
    public BusinessEntity findByName(String businessname) {
        return businessRepository.findById(businessname).orElse(null);
    }
    @Override //전체지역 가게명검색
    public List<BusinessEntity> searchBusinessesByName(String keyword) {
        return businessRepository.findByBusinessnameContaining(keyword);
    }

    @Override // 특정지역 + 가게명검색
    public List<BusinessEntity> searchBusinessesByZoneAndName(String zone, String keyword) {
        return businessRepository.findByZoneAndBusinessnameContaining(zone, keyword);
    }
}
