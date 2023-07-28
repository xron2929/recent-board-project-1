package com.example.demo.security.encorder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//  BCrypt 교체도 해야되네 ㅇㅅㅇ;;
@Component
public class PepperPasswordEncoder implements PasswordEncoder {

    private final String pepper = "fawbhhafwebkejfhau3h2q3ifuq32hqo23qo";
    private final PasswordEncoder delegate;

    public PepperPasswordEncoder() {
        this.delegate = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String saltedPassword = rawPassword + pepper;
        return delegate.encode(saltedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String saltedPassword = rawPassword + pepper;
        return delegate.matches(saltedPassword, encodedPassword);
    }
}
