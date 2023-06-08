package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Voiture;
import com.example.demo.utls.VoitureRowMapper;

// DAO -> DATA ACCESS OBJECT JDBC
@Repository
public class VoitureRepository implements IRepository<Voiture> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Voiture> findAll() {
		return jdbcTemplate.query("SELECT * FROM voiture", 
				new VoitureRowMapper());
	}

	@Override
	public Voiture findById(Long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM voiture WHERE id = ?", 
				new VoitureRowMapper(),
				new Object[] { id });
	}

	@Override
	public int save(Voiture o) {
		return jdbcTemplate.update("INSERT INTO voiture (marque, modele, couleur) VALUES (?, ?, ?)", 
				o.getMarque(), 
				o.getModele(), 
				o.getCouleur());
	}

	@Override
	public int update(Voiture o) {
		return jdbcTemplate.update("UPDATE voiture SET marque = ?, modele = ?, couleur = ? WHERE id = ?", 
				o.getMarque(), 
				o.getModele(), 
				o.getCouleur(),
				o.getId());
	}

	@Override
	public int deleteById(Long id) {
		return jdbcTemplate.update("DELETE FROM voiture WHERE id = ?", id);
	}

}
