package com.akatsuki.pioms.categorySecond.aggregate;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="category_second")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CategorySecond {
    @Id
    @Column(name = "category_second_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categorySecondCode;

    @Column(name = "category_second_name")
    private String categorySecondName;

    @Column(name = "category_second_enroll_date")
    private String categorySecondEnrollDate;

    @Column(name = "category_second_update_date")
    private String categorySecondUpdateDate;

    @ManyToOne
    @JoinColumn(name="category_first_code")
    private CategoryFirst categoryFirstCode;
}


