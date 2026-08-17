package com.DevCollab.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.NotificationEntity;

@Repository
public interface NotificationRepository
        extends JpaRepository<NotificationEntity, Long> {


    // All notifications of user
    List<NotificationEntity>
    findByUserIdOrderByCreatedAtDesc(Long userId);


    // Unread notifications
    List<NotificationEntity>
    findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);


    // Count unread
    long countByUserIdAndReadFalse(Long userId);


    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
