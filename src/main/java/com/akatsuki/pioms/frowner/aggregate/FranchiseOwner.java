package com.akatsuki.pioms.frowner.aggregate;



import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import java.time.LocalDateTime;



@Entity
@Table(name = "franchise_owner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FranchiseOwner {
    @Id
    @Column(name = "franchise_owner_code")
    private int franchiseOwnerCode;

    @Column(name = "franchise_owner_name")
    private String franchiseOwnerName;

    @Column(name = "franchise_owner_id")
    private String franchiseOwnerId;

    @Column(name = "franchise_owner_pwd")
    private String franchiseOwnerPwd;

    @Column(name = "franchise_owner_email")
    private String franchiseOwnerEmail;

    @Column(name = "franchise_owner_phone")
    private String franchiseOwnerPhone;

    @Column(name = "franchise_owner_enroll_date")
    private String franchiseOwnerEnrollDate;

    @Column(name = "franchise_owner_update_date")
    private String franchiseOwnerUpdateDate;

    @Column(name = "franchise_owner_delete_date")
    private String franchiseOwnerDeleteDate;
    @OneToOne(mappedBy = "franchiseOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Franchise franchise;

  
    public FranchiseOwner(FranchiseOwnerDTO franchiseOwner) {
        this.franchiseOwnerCode= franchiseOwner.getFranchiseOwnerCode();
        this.franchiseOwnerName= franchiseOwner.getFranchiseOwnerName();
        this.franchiseOwnerId= franchiseOwner.getFranchiseOwnerId();
        this.franchiseOwnerPwd= franchiseOwner.getFranchiseOwnerPwd();
        this.franchiseOwnerEmail= franchiseOwner.getFranchiseOwnerEmail();
        this.franchiseOwnerPhone= franchiseOwner.getFranchiseOwnerPhone();
        this.franchiseOwnerEnrollDate= franchiseOwner.getFranchiseOwnerEnrollDate();
        this.franchiseOwnerUpdateDate= franchiseOwner.getFranchiseOwnerUpdateDate();
        this.franchiseOwnerDeleteDate= franchiseOwner.getFranchiseOwnerDeleteDate();
    }



}
