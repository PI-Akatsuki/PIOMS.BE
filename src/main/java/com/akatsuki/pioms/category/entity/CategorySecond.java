package com.akatsuki.pioms.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category_second")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategorySecond {
    @Id
    @Column(name = "category_second_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_second_code;

    @Column(name = "category_second_name")
    private String category_second_name;

    @ManyToOne
    @JoinColumn(name="category_first_code")
    private CategoryFirst category_first_code;
}


