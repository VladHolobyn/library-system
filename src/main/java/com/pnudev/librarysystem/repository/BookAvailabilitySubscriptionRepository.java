package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.BookAvailabilitySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAvailabilitySubscriptionRepository extends JpaRepository<BookAvailabilitySubscription, Long> {

    List<BookAvailabilitySubscription> findAllByBookIdIs(Long bookId);

    void deleteByBookIdIsAndUserIdIs(Long bookId, Long userId);
}
