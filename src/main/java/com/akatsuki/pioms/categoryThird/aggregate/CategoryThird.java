package com.akatsuki.pioms.categoryThird.aggregate;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
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
    @Column(name = "category_third_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryThirdCode;

    @Column(name = "category_third_name")
    private String categoryThirdName;

    @Column(name = "category_third_enroll_date")
    @Temporal(TemporalType.TIMESTAMP)
    private String categoryThirdEnrollDate;

    @Column(name = "category_third_update_date")
    private String categoryThirdUpdateDate;

    @Column(name = "category_third_delete_date")
    private String categoryThirdDeleteDate;

    @Column(name = "category_second_code")
    private int categorySecondCode;

}
