package com.campusfood.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stall_id")
    private Long stallId;

    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;

    @PrePersist
    protected void onCreate() {
        if (this.checkinTime == null) this.checkinTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }
    public LocalDateTime getCheckinTime() { return checkinTime; }
    public void setCheckinTime(LocalDateTime checkinTime) { this.checkinTime = checkinTime; }
}
