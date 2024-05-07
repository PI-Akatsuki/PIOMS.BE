package com.akatsuki.pioms.category.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="category_third")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
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
