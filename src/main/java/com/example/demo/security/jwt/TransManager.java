package com.example.demo.security.jwt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransManager {
    public Trans getTrans(String inputTransData) {
        log.info("trans= '{}'",inputTransData);
        System.out.println(inputTransData=="남성");
        Trans trans;
        if(inputTransData.equals("남성")) {
            trans = Trans.MALE;
        }
        else if(inputTransData.equals("여성")) {
            trans = Trans.MALE;
        }
        else trans=Trans.UNSELECTED;
        return trans;
    }
}
