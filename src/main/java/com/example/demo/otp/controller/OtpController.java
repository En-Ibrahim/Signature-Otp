package com.example.demo.otp.controller;

import com.example.demo.otp.dto.OtpRequest;
import com.example.demo.otp.dto.OtpValidationRequest;
import com.example.demo.otp.dto.Response;
import com.example.demo.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    @Autowired
    private OtpService service;


    @PostMapping("/send")
    public Response sendOtp(@RequestBody OtpRequest otpRequest){
        return service.sendOtp(otpRequest);
    }

    @PostMapping("/validateOtp")
    public Response validateOtp(@RequestBody OtpValidationRequest otpValidationRequest){
        return service.validateOtp(otpValidationRequest);
    }


}
