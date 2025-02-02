package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.ApiUseTime;
import com.sparta.myselectshop.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime,Long> {
    Optional<ApiUseTime> findByUser(User user);
}
