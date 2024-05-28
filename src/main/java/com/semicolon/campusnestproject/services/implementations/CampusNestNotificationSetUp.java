package com.semicolon.campusnestproject.services.implementations;


import com.semicolon.campusnestproject.dtos.requests.DataSender;
import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;
import com.semicolon.campusnestproject.dtos.requests.ReceiverRequest;
import com.semicolon.campusnestproject.dtos.responses.NotificationResponse;
import com.semicolon.campusnestproject.services.NotificationSetUpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CampusNestNotificationSetUp implements NotificationSetUpService {

    @Value("${brevo.api.url}")
    private String url;
    @Value("${brevo.api.key}")
    private String apiKey;



    @Override
    public void sendNotification(NotificationSenderRequest senderRequest, String subject,
                                 String htmlContent) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key",apiKey);
        for (ReceiverRequest to : senderRequest.getTo()){
            DataSender dataSender = new DataSender();
            dataSender.setSender(senderRequest);
            dataSender.getTo().add(to);
            dataSender.setSubject(subject);
            dataSender.setHtmlContent(htmlContent);
            HttpEntity<?> httpEntity = new HttpEntity<>(dataSender,httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(url,httpEntity, NotificationResponse.class);
        }
    }
}
