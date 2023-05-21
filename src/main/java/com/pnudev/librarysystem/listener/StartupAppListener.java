package com.pnudev.librarysystem.listener;

import com.pnudev.librarysystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@RequiredArgsConstructor
@Component
public class StartupAppListener {

    private final UserService userService;

    @Value("${file.upload-dir}/books")
    private String uploadDir;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userService.createAdminUserIfNotExists();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
