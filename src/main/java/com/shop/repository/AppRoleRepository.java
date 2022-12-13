package com.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.common.ERole;
import com.shop.entitty.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long>{
	Optional<AppRole> findByName(ERole nam);

	@Query(value = "Delete From app_roles app \r\n"
			+ " where app.user_id = :id ", nativeQuery = true)
	void deleteByUserId(@Param("id") long id);
}
