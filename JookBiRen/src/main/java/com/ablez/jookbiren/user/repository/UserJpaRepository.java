package com.ablez.jookbiren.user.repository;

import com.ablez.jookbiren.user.entity.UserEp01;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEp01, Long> {
}
