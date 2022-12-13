package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shop.entitty.Category;
import com.shop.entitty.Product;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByStatusTrue();

	List<Product> findByStatusTrueOrderBySoldDesc();
	List<Product> findTop10ByOrderBySoldDesc();
	List<Product> findByStatusTrueOrderByQuantityDesc();
	List<Product> findByStatusTrueOrderByEnteredDateDesc();
	List<Product> findByCategoryAndStatusTrueAndQuantityGreaterThan(Category category, int quantity);

	List<Product> findByCategory_CategoryIdAndStatusTrue(Long id);
	Product findByProductIdAndStatusTrue(Long id);
	List<Product> findProductsByStatusTrueAndQuantityGreaterThan(int quantity);
	@Query(value = "Select p.* From products p \r\n"
			+ "left join rates r on p.product_id = r.product_id \r\n"
			+ "left join categories c on p.category_id = c.category_id \r\n"
			+ "where p.status=true and c.status = true \r\n"
			+ "group by p.product_id , p.name\r\n"
//			+ "where p.status = 1 \r\n"
			+ "Order by  avg(r.rating) desc, RAND()", nativeQuery = true)
	List<Product> findProductRated();

	@Query("select p from Product p where p.status=true and p.category.status=true and p.quantity>0")
	List<Product> findAllProducts();

	
	@Query(value = "(Select p.*, avg(r.rating) Rate From products p \r\n"
			+ "left join rates r on p.product_id = r.product_id\r\n"
			+ "Where (p.category_id = ?) and (p.product_id != ?)\r\n"
			+ "group by p.product_id , p.name)\r\n"
			+ "union\r\n"
			+ "(Select p.*, avg(r.rating) Rate From products p \r\n"
			+ "left join rates r on p.product_id = r.product_id\r\n"
			+ "Where p.category_id != ?\r\n"
			+ "group by p.product_id , p.name)\r\n"
			+ "Order by category_id = ? desc, Rate desc", nativeQuery = true)
	List<Product> findProductSuggest(Long id, Long id2, Long id3, Long id4);

	@Query(value = "Select p.* From products p \r\n"
			+ "left join categories c on p.category_id = c.category_id \r\n"
			+ "where p.quantity > 0 and p.status = true and c.status = true \r\n"
			+ "ORDER BY p.sold DESC ", nativeQuery = true)
	List<Product> findProductGreaterThanZeroSoldDesc();
	@Query(value = "Select p.* From products p \r\n"
			+ "left join categories c on p.category_id = c.category_id \n"
			+ "where p.quantity > 0 and p.status = true and c.status = true \r\n"
			+ "ORDER BY p.entered_date DESC ", nativeQuery = true)
	List<Product> findProductGreaterThanZeroEnteredDateDesc();

//	@Modifying
//	@Query(value = "Update products p set p.status = ?1 where p.category_id = ?2 ", nativeQuery = true)
//	void updateStatusAllProduct(long id, long status);

	List<Product> findByPriceBetweenAndStatusTrue(Double minPrice, Double maxPrice);
}
