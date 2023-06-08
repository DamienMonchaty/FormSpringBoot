package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Personne;
import com.example.demo.repository.PersonneRepository;

// COUCHE METIER -> BUSINESS LAYER
// Recupere l'injection de dependance du DAO
@Service
public class PersonneService implements IService<Personne> {
	
	@Autowired
	private PersonneRepository personneRepository;

	@Override
	public List<Personne> findAll() {
		return personneRepository.findAll();
	}

	@Override
	public Personne saveOrUpdate(Personne o) {
		if(o.getId() != null) {
			Personne pToEdit = getOne(o.getId()).get();
			pToEdit.setNom(o.getNom());
			pToEdit.setPrenom(o.getPrenom());
			pToEdit.setAge(o.getAge());
			return personneRepository.save(pToEdit);
		}
		return personneRepository.save(o);
	}

	@Override
	public Optional<Personne> getOne(long id) {
		return personneRepository.findById(id);
	}

	@Override
	public void delete(long id) {
		personneRepository.deleteById(id);		
	}

}
