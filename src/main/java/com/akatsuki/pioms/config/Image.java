package com.akatsuki.pioms.config;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
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
