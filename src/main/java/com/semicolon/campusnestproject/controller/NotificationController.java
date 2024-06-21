package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.CreatePostRequest;
import com.semicolon.campusnestproject.dtos.requests.SendNotificationRequest;
import com.semicolon.campusnestproject.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification/")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("sendNotification")
    public ResponseEntity<?> sendNotification(@RequestBody SendNotificationRequest request){
        try {
            notificationService.createNotification(request);
            return ResponseEntity.ok().body("notified");

        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("notifications/{id}")
    public ResponseEntity<?> getNotifications(@PathVariable Long id){
        try {
            List<String> messages = notificationService.listOfNotification(id);
            return ResponseEntity.ok().body(messages);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
