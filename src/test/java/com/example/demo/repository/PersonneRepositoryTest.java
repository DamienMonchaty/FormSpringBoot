package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.models.Personne;

@DataJpaTest // pour tester les JPA repositories
public class PersonneRepositoryTest {

	@Autowired
	private PersonneRepository personneRepository;

	@BeforeEach
	public void initEach() {
		// test setup code
	}

	@Test
	public void testFindAll() {
		Personne p1 = new Personne("NOM1", "PRENOM1", 40);
		Personne p2 = new Personne("NOM2", "PRENOM2", 30);

		List<Personne> personnes = Arrays.asList(p1, p2);

		personneRepository.saveAll(personnes);

		List<Personne> personnesFromDb = personneRepository.findAll();

		assertEquals(personnes, personnesFromDb);
		assertThat(personnesFromDb.equals(personnes));
	}

	@Test
	public void testFindById() {
		Personne savedInDb = personneRepository.save(new Personne("NOM1", "PRENOM1", 40));
		Personne getFromDb = personneRepository.findById(savedInDb.getId()).get();

		assertEquals(savedInDb, getFromDb);
		assertThat(savedInDb).isEqualTo(getFromDb);
	}

	@Test
	public void testSave() {
		Personne personneToSave = new Personne("NOM1", "PRENOM1", 40);
		Personne personneSaved = personneRepository.save(personneToSave);

		assertEquals(personneToSave.getNom(), personneSaved.getNom());
		assertThat(personneToSave).isEqualTo(personneSaved);
	}

	// exo -> Veuillez ecrire la methode de Test Delete

	@Test
	public void testDelete() {
		Personne p1 = new Personne("Wick", "John", 40);
		Personne p2 = new Personne("Doe", "Bob", 30);

		personneRepository.save(p1);
		personneRepository.save(p2);

		List<Personne> personnes = Arrays.asList(p1, p2);

		Personne getFromDb = personneRepository.findById(p2.getId()).get();
		personneRepository.deleteById(getFromDb.getId());

		List<Personne> personListFromDb = personneRepository.findAll();

		assertNotEquals(personnes, personListFromDb);
		assertTrue(!personListFromDb.contains(getFromDb));
		assertFalse(personListFromDb.contains(getFromDb));
		assertThat(personListFromDb.contains(getFromDb)).isFalse();
	}

	@Test()
	public void testNoSuchElementException() {
		assertThrows(NoSuchElementException.class, () -> {
			personneRepository.findById(5L).get();
		});
	}
}
