package com.jit;


import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("frank");
        System.out.println(encodedPassword);

    }
}
