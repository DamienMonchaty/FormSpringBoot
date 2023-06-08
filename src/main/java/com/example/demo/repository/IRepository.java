package com.example.demo.repository;

import java.util.List;

public interface IRepository<T> {
	
	List<T> findAll();
	T findById(Long id);
	int save(T o);
	int update(T o);
	int deleteById(Long id);
}
