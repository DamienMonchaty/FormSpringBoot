package com.example.demo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.models.Voiture;
import com.example.demo.repository.IRepository;

/**
 * The Class VoitureRestController.
 */

//COUCHE MAPPING CHEMIN SUR DES Methodes
//Recupere l'injection de dependance du bean voitureRepository
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/voitures")
public class VoitureRestController {

	/** The voiture repository. */
	@Autowired
	private IRepository<Voiture> voitureRepository;

	/** The model mapper. */
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	@GetMapping()
	public ResponseEntity<List<Voiture>> getAll() {
		return new ResponseEntity<>(voitureRepository.findAll(), HttpStatus.OK);
	}

	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") long id) {
		try {
			Voiture v = voitureRepository.findById(id);
			return new ResponseEntity<>(v, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Voiture avec " + id + " n'existe pas !", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Save.
	 *
	 * @param voiture the voiture
	 * @return the response entity
	 */
	@PostMapping()
	public ResponseEntity<Voiture> save(@RequestBody Voiture voiture) {
		voitureRepository.save(voiture);
		return new ResponseEntity<>(voiture, HttpStatus.CREATED);
	}

	/**
	 * Edits the.
	 *
	 * @param voiture the voiture
	 * @param id the id
	 * @return the response entity
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@RequestBody Voiture voiture, @PathVariable("id") long id) {
		try {
			Voiture vToEdit = voitureRepository.findById(id);
			this.modelMapper.map(voiture, vToEdit);
			voitureRepository.update(vToEdit);
			return new ResponseEntity<>(vToEdit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Voiture avec " + id + " n'existe pas !", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		try {
			Voiture vToDelete = voitureRepository.findById(id);
			voitureRepository.deleteById(vToDelete.getId());
			return new ResponseEntity<>("Voiture supprimée " + id + " avec Succès !", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Voiture avec " + id + " n'existe pas !", HttpStatus.NOT_FOUND);
		}
	}

}
