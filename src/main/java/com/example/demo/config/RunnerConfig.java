package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.models.Voiture;
import com.example.demo.repository.IRepository;

@Component
public class RunnerConfig implements CommandLineRunner {

	@Autowired
	private IRepository<Voiture> voitureRepository;
	
	@Override
	public void run(String... args) throws Exception {
//		voitureRepository.save(new Voiture("MARQUE1", "MODELE1", "COULEUR1"));	
//		voitureRepository.save(new Voiture("MARQUE2", "MODELE2", "COULEUR2"));		
//		voitureRepository.save(new Voiture("MARQUE3", "MODELE3", "COULEUR3"));		
//		voitureRepository.save(new Voiture("MARQUE4", "MODELE4", "COULEUR4"));		
//		voitureRepository.save(new Voiture("MARQUE5", "MODELE5", "COULEUR5"));		
	}

}
