package com.akatsuki.pioms.category.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="category_first")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class CategoryFirst {
    @Id
    @Column(name = "category_first_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_first_code;

    @Column(name = "category_first_name")
    private String category_first_name;

    @Column(name = "category_first_enroll_date")
    private String category_first_enroll_date;

    @Column(name = "category_first_update_date")
    private String category_first_update_date;
}

