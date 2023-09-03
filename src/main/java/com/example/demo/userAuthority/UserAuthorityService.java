package com.example.demo.userAuthority;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityService {
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    public UserAuthority saveAuthority(UserAuthority userAuthority) {
        System.out.println("??");
        return userAuthorityRepository.save(userAuthority);
    }

}
