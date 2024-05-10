package com.akatsuki.pioms.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="category_first")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoryFirst {
    @Id
    @Column(name = "category_first_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_first_code;

    @Column(name = "category_first_name")
    private String category_first_name;

    @Column(name = "category_first_enroll_date")
    private Date category_first_enroll_date;

    @Column(name = "category_first_update_date")
    private Date category_first_update_date;
}

