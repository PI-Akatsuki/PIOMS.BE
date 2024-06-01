package com.akatsuki.pioms.config;


import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.user.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ConvertUser {

    private final AdminRepository adminInfoRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;

    @Autowired
    public ConvertUser(AdminRepository adminInfoRepository, DeliveryDriverRepository deliveryDriverRepository, FranchiseOwnerRepository franchiseOwnerRepository) {
        this.adminInfoRepository = adminInfoRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
    }

    public int getCode(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userName = authentication.getName();
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // 권한 정보
//        if (!ROLE.equals(role))
//            return -1;
        System.out.println("role = " + role);
        System.out.println("userName = " + userName);
        if (role.equals("ROLE_ADMIN") ||role.equals("ROLE_ROOT"))
            return adminInfoRepository.findByAdminId(userName).get().getAdminCode();
        if (role.equals("ROLE_Driver"))
            return deliveryDriverRepository.findByDriverId(userName).get().getDriverCode();
        return franchiseOwnerRepository.findByFranchiseOwnerId(userName).get().getFranchiseOwnerCode();
    }

    public String getName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userName = authentication.getName();
        return userName;
    }

}
