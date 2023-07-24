package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.entity.BookAvailabilitySubscription;
import com.pnudev.librarysystem.repository.BookAvailabilitySubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookAvailabilitySubscriptionService {

    @Value("${spring.mail.username}")
    private String appEmail;

    private final BookAvailabilitySubscriptionRepository subscriptionRepository;
    private final JavaMailSender mailSender;


    public void subscribe(Long bookId, Long userId) {
        BookAvailabilitySubscription subscription = BookAvailabilitySubscription.builder()
                .userId(userId)
                .bookId(bookId)
                .build();

        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(Long bookId, Long userId) {
        subscriptionRepository.deleteByBookIdIsAndUserIdIs(bookId, userId);
    }

    @Async
    public void notify(Long bookId) {
        List<BookAvailabilitySubscription> subscriptionList = subscriptionRepository.findAllByBookIdIs(bookId);

        subscriptionList.forEach(
                notification -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(appEmail);
                    message.setTo(notification.getUser().getEmail());
                    message.setSubject("Book is available!!");
                    message.setText("Book(%s) is available!! Now you can reserve this book."
                            .formatted(notification.getBook().getTitle())
                    );
                    mailSender.send(message);
                }
        );

        subscriptionRepository.deleteAll(subscriptionList);
    }

}
