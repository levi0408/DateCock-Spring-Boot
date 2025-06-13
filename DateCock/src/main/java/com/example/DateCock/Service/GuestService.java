package com.example.DateCock.Service;

import com.example.DateCock.DTO.GuestDTO;
import com.example.DateCock.Entity.GuestEntity;

import java.sql.Timestamp;
import java.util.List;

public interface GuestService {
    GuestEntity insertGuest(GuestDTO dto);

    List<GuestEntity> select();

    void deleteGuest(String name, Timestamp todays);
}
