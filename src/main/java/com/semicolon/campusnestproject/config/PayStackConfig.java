package com.semicolon.campusnestproject.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PayStackConfig {

    @Value("${paystack.base.url}")
    private String payStackBaseUrl;
    @Value("${paystack.api.key}")
    private String payStackApiKey;
    @Value("${paystack.base.transfer.url}")
    private String payStackTransferUrl;
}
