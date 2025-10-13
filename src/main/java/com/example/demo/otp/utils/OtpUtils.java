package com.example.demo.otp.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateOtp(){
        StringBuilder otp= new StringBuilder();
        Random random= new Random();
        int count =0;
        while (count<6){
            otp.append(random.nextInt(10));
            ++count;
        }
        return otp.toString();
    }


}
