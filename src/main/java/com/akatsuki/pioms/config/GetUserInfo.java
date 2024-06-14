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
public class GetUserInfo {

    private final AdminRepository adminInfoRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;

    @Autowired
    public GetUserInfo(AdminRepository adminInfoRepository, DeliveryDriverRepository deliveryDriverRepository, FranchiseOwnerRepository franchiseOwnerRepository) {
        this.adminInfoRepository = adminInfoRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
    }

    public int getAdminCode(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println("userName = " + userName);
        return adminInfoRepository.findByAdminId(userName).get().getAdminCode();
    }

    public boolean isRoot(int adminCode){
        return "ROLE_ROOT".equals(adminInfoRepository.findById(adminCode).get().getAdminRole());
    }
    public int getFranchiseOwnerCode(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println("userName = " + userName);
        return franchiseOwnerRepository.findByFranchiseOwnerId(userName).get().getFranchiseOwnerCode();
    }
    public int getDriverCode(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println("userName = " + userName);
        return deliveryDriverRepository.findByDriverId(userName).get().getDriverCode();
    }

    public String getName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userName = authentication.getName();
        return userName;
    }

}