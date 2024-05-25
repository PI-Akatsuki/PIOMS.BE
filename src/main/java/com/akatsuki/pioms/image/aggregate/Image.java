package com.akatsuki.pioms.image.aggregate;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_code")
    private int productImageCode;

    @Column(name = "url")
    private String url;

    @Column(name = "product_code")
    private int productCode;


    public Image(String mediaLink, int productCode) {
        this.url= mediaLink;
        this.productCode = productCode;
    }
}
