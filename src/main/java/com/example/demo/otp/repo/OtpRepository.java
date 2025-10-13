package com.example.demo.otp.repo;

import com.example.demo.otp.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {

    Otp findByEmail(String email);
}
