package com.akatsuki.pioms.user.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Admin admin;
    private final FranchiseOwner franchiseOwner;
    private final DeliveryDriver deliveryDriver;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Admin admin, Collection<? extends GrantedAuthority> authorities) {
        this.admin = admin;
        this.franchiseOwner = null;
        this.deliveryDriver = null;
        this.authorities = authorities;
    }

    public CustomUserDetails(FranchiseOwner franchiseOwner, Collection<? extends GrantedAuthority> authorities) {
        this.admin = null;
        this.franchiseOwner = franchiseOwner;
        this.deliveryDriver = null;
        this.authorities = authorities;
    }

    public CustomUserDetails(DeliveryDriver deliveryDriver, Collection<? extends GrantedAuthority> authorities) {
        this.admin = null;
        this.franchiseOwner = null;
        this.deliveryDriver = deliveryDriver;
        this.authorities = authorities;
    }

    public Object getUser() {
        if (admin != null) {
            return admin;
        } else if (franchiseOwner != null) {
            return franchiseOwner;
        } else {
            return deliveryDriver;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        if (admin != null) {
            return admin.getAdminPwd();
        } else if (franchiseOwner != null) {
            return franchiseOwner.getFranchiseOwnerPwd();
        } else {
            return deliveryDriver.getDriverPwd();
        }
    }

    @Override
    public String getUsername() {
        if (admin != null) {
            return admin.getAdminId();
        } else if (franchiseOwner != null) {
            return franchiseOwner.getFranchiseOwnerId();
        } else {
            return deliveryDriver.getDriverId();
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
