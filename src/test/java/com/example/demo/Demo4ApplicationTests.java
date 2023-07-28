package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class Demo4ApplicationTests {

    @Test
    void contextLoads() {
        String a = "";
        String b = null;
        String c = " ";
        if(a.isBlank()) {
            System.out.println("error ");
        }
        if(b == null || b.isBlank()) {
            System.out.println("error 2");
        }
        if(c.isBlank()) {
            System.out.println("error 3");
        }
        if(a.isEmpty()) {
            System.out.println("error 4");
        }
        if(b == null || b.isEmpty()) {
            System.out.println("error 5");
        }
        if(c.isEmpty()) {
            System.out.println("error 6");
        }
    }

}
