package com.example.demo.controllers;

import java.util.List;

import javax.jms.Topic;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.demo.dto.PersonneDto;
import com.example.demo.models.Personne;
import com.example.demo.services.IService;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
// COUCHE MAPPING CHEMIN SUR DES Methodes
/**
 * The Class PersonneRestController.
 */
// Recupere l'injection de dependance du bean personneService

/** The Constant log. */
@Slf4j
@RestController
// Autorise la communication entre application (BACK-END <-> FRONT-END)
@CrossOrigin(origins = "*")
@RequestMapping(value = "/personnes")
public class PersonneRestController {

	
	
	/** The jms template. */
	@Autowired
	JmsTemplate jmsTemplate;

	/** The personne service. */
	@Autowired
	private IService<Personne> personneService;

	/** The model mapper. */
	@Autowired
	private ModelMapper modelMapper;

	// GET http://localhost:8080/personnes
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	// Affiche une liste de personnes sur le chemin /personnes
	@GetMapping()
	public ResponseEntity<List<Personne>> getAll() {
		return new ResponseEntity<>(personneService.findAll(), HttpStatus.OK); // Retourne la liste de personnes et HTTP																			
	}

	// GET http://localhost:8080/personnes/1
	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	// Affiche un personne selon son identifiant sur le chemin /personnes/1
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Personne> getById(@PathVariable("id") long id) {
		Personne p = personneService.getOne(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne avec l'id " + id + "n'existe pas !"));
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	// POST http://localhost:8080/personnes
	/**
	 * Save.
	 *
	 * @param personneDto the personne dto
	 * @return the response entity
	 */
	// Enregistre une objet de type Personne en db sur le chemin /personnes
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PostMapping()
	public ResponseEntity<Personne> save(@Valid @RequestBody PersonneDto personneDto) {
		try {
			
			Topic personneTopic = jmsTemplate.getConnectionFactory().createConnection()
					.createSession().createTopic("PersonneTopic");
			
			Personne personneToSave = this.modelMapper.map(personneDto, Personne.class);
			
			Personne personneSaved = personneService.saveOrUpdate(personneToSave);
			
			log.info("ENVOI D'UN OBJET PERSONNE -> " + personneSaved);
			
			jmsTemplate.convertAndSend(personneTopic, personneSaved);
			
			return new ResponseEntity<>(personneSaved, HttpStatus.CREATED); 
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}

	// PUT http://localhost:8080/personnes/1
	/**
	 * Edits the.
	 *
	 * @param personneDto the personne dto
	 * @param id the id
	 * @return the response entity
	 */
	// Met a jour un personne selon son identifiant sur le chemin /personnes/1
	@PutMapping(value = "/{id}")
	public ResponseEntity<Personne> edit(@Valid @RequestBody PersonneDto personneDto, @PathVariable("id") long id) {
		Personne pToEdit = personneService.getOne(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found with id : " + id));
		this.modelMapper.map(personneDto, pToEdit);
		personneService.saveOrUpdate(pToEdit);
		return new ResponseEntity<>(pToEdit, HttpStatus.OK);
	}

	// DELETE http://localhost:8080/personnes/1
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	// Supprime une personne selon son identifiant sur le chemin /personnes/1
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		Personne p = personneService.getOne(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne avec l'id " + id + "n'existe pas !"));
		personneService.delete(p.getId());
		return new ResponseEntity<>("Personne supprimée " + id + " avec Succès !", HttpStatus.OK);
	}

}
