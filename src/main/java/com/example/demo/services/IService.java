package com.example.demo.services;

import java.util.List;
import java.util.Optional;

public interface IService<T> {

	List<T> findAll();
	
	T saveOrUpdate(T o);
	
	Optional<T> getOne(long id);
	
	void delete(long id);
}
