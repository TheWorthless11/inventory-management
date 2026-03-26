package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.entity.Users; // <- use Users, not User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username); // <- also Users here
}