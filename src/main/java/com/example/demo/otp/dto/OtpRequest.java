package com.example.demo.otp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor@NoArgsConstructor
public class OtpRequest {

    private String email;


}
