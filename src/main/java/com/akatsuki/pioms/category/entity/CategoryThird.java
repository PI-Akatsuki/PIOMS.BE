package com.akatsuki.pioms.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category_third")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryThird {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_third_code;

    @Column
    private String category_third_name;

    @ManyToOne
    @JoinColumn(name="category_second_code")
    private CategorySecond category_second_code;
}
