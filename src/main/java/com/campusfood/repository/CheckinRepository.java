package com.campusfood.repository;

import com.campusfood.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    @Query(value = "SELECT COUNT(*) / 14.0 FROM checkins WHERE stall_id = :stallId AND HOUR(checkin_time) = :hour AND checkin_time >= DATE_SUB(CURDATE(), INTERVAL 14 DAY)", nativeQuery = true)
    Double getAverageCheckinsByHour(@Param("stallId") Long stallId, @Param("hour") int hour);
}
