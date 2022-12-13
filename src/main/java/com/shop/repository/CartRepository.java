package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entitty.Cart;
import com.shop.entitty.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findByUser(User user);
	@Query(value = "Delete From cart c2 \r\n"
			+ " where c2.user_id = :id ", nativeQuery = true)
	void deleteByUserId(@Param("id") long  id);
}
