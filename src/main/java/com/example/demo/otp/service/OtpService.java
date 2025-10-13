package com.example.demo.otp.service;

import com.example.demo.otp.dto.*;
import com.example.demo.otp.entity.Otp;
import com.example.demo.otp.repo.OtpRepository;
import com.example.demo.otp.utils.OtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public Response sendOtp(OtpRequest otpRequest){

        //generate otp
        //send otp
        //save otp

        String otp = OtpUtils.generateOtp();
        log.info("OTP: {}",otp);

        otpRepository.save(Otp.builder()
                        .email(otpRequest.getEmail())
                        .otp(otp)
                        .expiresAt(LocalDateTime.now().plusMinutes(2))
                .build());

        emailService.sendEmail(EmailDetails.builder()
                        .subject("Do Not DISCLOSE")
                        .recipient(otpRequest.getEmail())
                        .messageBody("This organization has sent otp. this otp is expires in 2 minutes. "+otp)
                .build());

        return Response.builder()
                .statuesCode(200)
                .responseMessage("Success")
                .userInfo(otpRequest.getEmail())
                .build();
    }






    public Response validateOtp(OtpValidationRequest otpValidationRequest){
        Otp otp= otpRepository.findByEmail(otpValidationRequest.getEmail());
        log.info("Email is {}",otpValidationRequest.getEmail());

        if (otp==null){
            return Response.builder()
                    .statuesCode(400)
                    .responseMessage("you have not sent OTP")
                    .build();
        }

        if(otp.getExpiresAt().isBefore(LocalDateTime.now())){
            return Response.builder()
                    .statuesCode(400)
                    .responseMessage("Expired OTP")
                    .build();
        }
        if (!otp.getOtp().equals(otpValidationRequest.getOtp())){
            return Response.builder()
                    .statuesCode(400)
                    .responseMessage("Invalid OTP")
                    .build();
        }
        return Response.builder()
                .statuesCode(200)
                .responseMessage("Success!! ")
                .isOtpValidate(OtpResponse.builder()
                        .isOtpValidate(true)
                        .build())
                .build();

    }

}
