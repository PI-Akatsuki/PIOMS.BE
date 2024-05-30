package com.akatsuki.pioms.user.aggregate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private int userCode;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String password;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_access_number")
    private String accessNumber;

    @Column(name = "user_role")
    private String  Role;

    @Column(name = "user_status")
    private boolean userStatus;

    @Column(name = "user_pwd_check")
    private int pwdCheckCount;
    
    @Column(name = "user_enroll_date")
    private String enrollDate;

    @Column(name = "user_update_date")
    private String updateDate;

    @Column(name = "user_delete_date")
    private String deleteDate;

}
