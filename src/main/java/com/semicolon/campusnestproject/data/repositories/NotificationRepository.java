package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
