package com.example.SPP.Service;

import com.example.SPP.Model.User;
import java.util.Optional;

public interface UserService {
    User authenticate(String username, String password);
    Optional<User> findByUsername(String username);
    User saveUser(User user);
    
    // Delete methods - support both ID and username
    void delete(Long id);                    // Delete by ID
    void deleteByUsername(String username);  // Delete by username
    
    // Additional useful methods
    boolean existsByUsername(String username);
    Optional<User> findById(Long id);
}