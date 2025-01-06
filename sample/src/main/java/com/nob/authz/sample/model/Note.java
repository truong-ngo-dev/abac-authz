package com.nob.authz.sample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Note {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String userId;
    private String createdBy;
    private LocalDate dueDate;
}
