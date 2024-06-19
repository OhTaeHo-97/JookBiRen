package com.ablez.jookbiren.user.repository;

import com.ablez.jookbiren.user.entity.UserInfoEp01;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoJpaRepository extends JpaRepository<UserInfoEp01, Long> {
}
