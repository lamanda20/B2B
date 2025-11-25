package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private boolean seen;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.seen = false;
    }
}
