package com.akatsuki.pioms.admin.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin")
public class AdminEntity {
    @Id
    @Column(name = "admin_code")
    private int adminCode;
    @Column(name = "admin_name")
    private String adminName;

}
