package com.AppRH.AppRH.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Item;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
	 Page<Item> findAll(Pageable pageable);
}
