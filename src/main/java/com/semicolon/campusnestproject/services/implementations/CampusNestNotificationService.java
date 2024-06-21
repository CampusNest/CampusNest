package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.Notification;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.NotificationRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.SendNotificationRequest;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CampusNestNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @Override
    public void createNotification(SendNotificationRequest sendNotificationRequest) {
        User user = userRepository.findById(sendNotificationRequest.getUserId()).orElseThrow(()-> new UserNotFoundException("user not found"));

        Notification notification = Notification.builder()
                                      .message(sendNotificationRequest.getMessage())
                                    .build();
        user.getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(user);

    }

    @Override
    public List<String> listOfNotification(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user not found"));

        List<String> messages = new ArrayList<>();

        for (Notification notification : user.getNotifications()){
            messages.add(notification.getMessage());
        }

        return messages;
    }
}
