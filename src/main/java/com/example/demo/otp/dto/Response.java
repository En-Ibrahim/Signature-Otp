package com.example.demo.otp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude( JsonInclude.Include.NON_NULL)
public class Response {

    private int statuesCode;
    private String responseMessage;
    private String userInfo;
    private OtpResponse isOtpValidate;

}
