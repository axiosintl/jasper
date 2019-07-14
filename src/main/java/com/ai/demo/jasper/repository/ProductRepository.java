package com.ai.demo.jasper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ai.demo.jasper.entity.Product;

@Repository("productRepository")
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
