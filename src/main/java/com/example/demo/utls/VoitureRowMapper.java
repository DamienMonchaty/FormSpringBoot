package com.example.demo.utls;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.models.Voiture;

public class VoitureRowMapper implements RowMapper<Voiture> {

	@Override
	public Voiture mapRow(ResultSet rs, int rowNum) throws SQLException {
		Voiture voiture = new Voiture();
		voiture.setId(rs.getLong("id"));
		voiture.setMarque(rs.getString("marque"));
		voiture.setModele(rs.getString("modele"));
		voiture.setCouleur(rs.getString("couleur"));
		return voiture;
	}

}
