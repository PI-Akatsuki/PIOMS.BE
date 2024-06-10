package com.akatsuki.pioms.user.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.user.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    public CustomUserDetailsService(
            AdminRepository adminRepository,
            FranchiseOwnerRepository franchiseOwnerRepository,
            DeliveryDriverRepository deliveryDriverRepository
    ) {
        this.adminRepository = adminRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByAdminId(username);
        if (admin.isPresent()) {
            Admin adminUser = admin.get();
            Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(adminUser.getAdminRole()));
            return new CustomUserDetails(adminUser, authorities);
        }

        Optional<FranchiseOwner> franchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerId(username);
        if (franchiseOwner.isPresent()) {
            FranchiseOwner ownerUser = franchiseOwner.get();
            Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ownerUser.getFranchiseRole()));
            return new CustomUserDetails(ownerUser, authorities);
        }

        Optional<DeliveryDriver> deliveryDriver = deliveryDriverRepository.findByDriverId(username);
        if (deliveryDriver.isPresent()) {
            DeliveryDriver driverUser = deliveryDriver.get();
            Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(driverUser.getDriverRole()));
            return new CustomUserDetails(driverUser, authorities);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public String getUserRole(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }
}
