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
    private int category_second_code;

    @Column(name = "category_second_name")
    private String category_second_name;

    @Column(name = "category_second_enroll_date")
    private String category_second_enroll_date;

    @Column(name = "category_second_update_date")
    private String category_second_update_date;

    @ManyToOne
    @JoinColumn(name="category_first_code")
    private CategoryFirst category_first_code;
}


