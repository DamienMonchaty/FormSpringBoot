package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Personne;

//DAO -> DATA ACCESS OBJECT HIBERNATE
@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {

}
