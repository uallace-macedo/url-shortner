package com.java_api.repository;

import com.java_api.model.Click;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickRepository extends JpaRepository<Click, Long> {
    List<Click> findByUrlId(Long urlId);
}
