package com.example.DateCock.Service;

import com.example.DateCock.DTO.GuestDTO;
import com.example.DateCock.Entity.GuestEntity;
import com.example.DateCock.Repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GuestServiceImp implements GuestService {
    @Autowired
    GuestRepository guestRepository;
    @Override
    public GuestEntity insertGuest(GuestDTO dto) {
        GuestEntity entity = new GuestEntity();
        entity.setName(dto.getName());
        entity.setMemo(dto.getMemo());
        entity.setTodays(new Timestamp(System.currentTimeMillis()));

        return guestRepository.save(entity);
    }

    @Override
    public List<GuestEntity> select() {
        return guestRepository.findAll();
    }
    @Transactional
    @Override
    public void deleteGuest(String name, Timestamp todays) {
        guestRepository.deleteByNameAndTodays(name, todays);
    }
}
