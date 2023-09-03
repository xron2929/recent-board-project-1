package com.example.demo.gender;

import com.example.demo.security.jwt.Trans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService {
    @Autowired GenderRepository genderRepository;
    public void setGender(Trans trans) {
        Gender gender = new Gender(trans);
        genderRepository.save(gender);
    }
}
