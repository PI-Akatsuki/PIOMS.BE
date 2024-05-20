package com.akatsuki.pioms.categoryThird.aggregate;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="category_third")
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
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

    @ManyToOne
    @JoinColumn(name = "category_second_code")
    private CategorySecond categorySecond;

    public CategoryThird(CategoryThirdDTO categoryThird) {
        this.categoryThirdCode = categoryThird.getCategoryThirdCode();
        this.categoryThirdName = categoryThird.getCategoryThirdName();
        this.categoryThirdEnrollDate = categoryThird.getCategoryThirdEnrollDate();
        this.categoryThirdUpdateDate = categoryThird.getCategoryThirdUpdateDate();

    }

    public CategoryThird(int categorySecondCode) {
        this.categorySecond = new CategorySecond(categorySecondCode);
    }

    public CategoryThird() {
        this.categorySecond = new CategorySecond();
    }

}
