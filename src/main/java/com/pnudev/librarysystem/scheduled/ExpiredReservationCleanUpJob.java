package com.pnudev.librarysystem.scheduled;

import com.pnudev.librarysystem.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExpiredReservationCleanUpJob {
    private final BorrowingService borrowingService;

    @Scheduled(cron = "@daily")
    public void cleanUpExpiredReservation() {
        borrowingService.cleanUpExpiredReservation();
    }
}
