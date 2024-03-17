package com.cp.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cp.backend.entity.Auth;

@Repository
public interface AuthRepo extends JpaRepository<Auth, Long> {

    Optional<Auth> findOneByUsername(String username);

}
