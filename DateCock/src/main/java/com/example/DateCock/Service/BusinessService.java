package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;

import java.util.List;

public interface BusinessService {
    void insertbusiness(BusinessEntity businessEntity);

    BusinessEntity allout(String businessnumber);

    BusinessEntity updatego(String businessnumber);

    void updateall(BusinessEntity businessEntity);

    void deleteall(String businessnumber);

    List<BusinessEntity> allselect();

    BusinessEntity datereservationOut(String businessname);

    String businessAddressOut(String businessname);

    BusinessEntity findByName(String businessname);

    List<BusinessEntity> searchBusinessesByName(String keyword);

    List<BusinessEntity> searchBusinessesByZoneAndName(String zone, String keyword);
}
