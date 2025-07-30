package com.example.SPP.Repository;

import com.example.SPP.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Check if user exists by username
    boolean existsByUsername(String username);
    
    // Delete user by username
    void deleteByUsername(String username);
    
    // Find user by username and password (for authentication)
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    // Additional useful methods
    Optional<User> findByUsernameIgnoreCase(String username);
}