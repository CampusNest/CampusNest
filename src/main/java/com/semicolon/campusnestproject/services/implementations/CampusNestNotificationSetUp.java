package com.semicolon.campusnestproject.services.implementations;


import com.semicolon.campusnestproject.dtos.requests.DataSender;
import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;
import com.semicolon.campusnestproject.dtos.requests.ReceiverRequest;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentNotificationSenderRequest;
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

    @Value("${brevo.base.url}")
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

    @Override
    public void updateLandLordApartmentNotification(UpdateApartmentNotificationSenderRequest senderRequest, String subject, String htmlContent) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key",apiKey);
        UpdateApartmentDataSender updateApartmentDataSender =
                new UpdateApartmentDataSender();
        updateApartmentDataSender.setSender(senderRequest);
        updateApartmentDataSender.setRecipient(senderRequest.getRecipient());
        updateApartmentDataSender.setSubject(subject);
        updateApartmentDataSender.setHtmlContent(htmlContent);
        HttpEntity<?> httpEntity = new HttpEntity<>(updateApartmentDataSender,httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url,httpEntity, NotificationResponse.class);

    }
}
