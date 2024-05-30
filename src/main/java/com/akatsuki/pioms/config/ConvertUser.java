package com.akatsuki.pioms.config;


import com.akatsuki.pioms.admin.service.AdminInfoService;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import com.akatsuki.pioms.user.aggregate.User;
import com.akatsuki.pioms.user.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ConvertUser {

    private final UserDetailsService userDetailsService;
    private final AdminInfoService adminInfoService;
    private final DeliveryDriverService deliveryDriverService;
    private final FranchiseOwnerService franchiseOwnerService;
    @Autowired
    public ConvertUser(UserDetailsService userDetailsService, AdminInfoService adminInfoService, DeliveryDriverService deliveryDriverService, FranchiseOwnerService franchiseOwnerService) {
        this.userDetailsService = userDetailsService;
        this.adminInfoService = adminInfoService;
        this.deliveryDriverService = deliveryDriverService;
        this.franchiseOwnerService = franchiseOwnerService;
    }

    public int convertUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userName = userDetails.getUser().getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // 권한 정보
        System.out.println("role = " + role);
        if (role.equals("ROLE_ADMIN") ||role.equals("ROLE_ROOT"))
            return adminInfoService.findAdminCodeByName(userName);
        if (role.equals("ROLE_Driver"))
            return deliveryDriverService.findDriverCodeByName(userName);
//        if (role.equals("ROLE_OWNER"))
        return franchiseOwnerService.findFranchiseOwnerCodeByName(userName);
    }
}
