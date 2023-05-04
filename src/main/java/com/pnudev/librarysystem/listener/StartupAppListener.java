package com.pnudev.librarysystem.listener;

import com.pnudev.librarysystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StartupAppListener {

    private final UserService userService;


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userService.createAdminUserIfNotExists();
    }
}