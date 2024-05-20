package com.akatsuki.pioms.categoryFirst.aggregate;

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
    private int categoryFirstCode;

    @Column(name = "category_first_name")
    private String categoryFirstName;

    @Column(name = "category_first_enroll_date")
    private String categoryFirstEnrollDate;

    @Column(name = "category_first_update_date")
    private String categoryFirstUpdateDate;

    public CategoryFirst(int categoryFirstCode) {
        this.categoryFirstCode = categoryFirstCode;
    }
}

