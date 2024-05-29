package com.akatsuki.pioms.user.repository;

import com.akatsuki.pioms.user.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(String userId);

    User findByUsername(String username);
}
