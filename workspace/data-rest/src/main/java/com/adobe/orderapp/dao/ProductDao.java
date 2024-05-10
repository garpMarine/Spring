package com.adobe.orderapp.dao;

import com.adobe.orderapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    // select * from products where quantity = ?
    List<Product> findByQuantity(int q);

    // ResultSet executeQuery(SQL);
    //select * from products where price >= 1000 and price <= 10000;
   // @Query(value = "select * from products where price >= :l and price <= :h", nativeQuery = true)
    @Query("from Product where price >= :l and price <= :h")
    List<Product> getByRange(@Param("l") double low, @Param("h") double high);

    // int executeUpdate(SQL)
    @Modifying
    @Query("update Product set price = :pr where id = :id")
    void updateProduct(@Param("id") int id, @Param("pr") double price);
}
