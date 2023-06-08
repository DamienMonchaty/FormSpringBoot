package com.example.demo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.models.Personne;
import com.example.demo.repository.PersonneRepository;

@ExtendWith(MockitoExtension.class)
public class PersonneServiceTest {

	@InjectMocks
	private PersonneService personneService;
	
	@Mock
	private PersonneRepository personneRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
    
    @Test
    public void testFindAll() {
		Personne p1 = new Personne("Wick", "John", 40);
		Personne p2 = new Personne("Doe", "Bob", 30);

		List<Personne> personnes = Arrays.asList(p1, p2);
		
		Mockito.when(personneRepository.findAll()).thenReturn(personnes);
		
		List<Personne> personnesFromDb = personneService.findAll();
		
		assertNotNull(personnesFromDb);
		assertEquals(personnesFromDb, personnes);
		
		verify(personneRepository).findAll();				
    }
    
    @Test
    public void testSaveOrUpdate(){
        Personne p1 = new Personne("NOM1", "PRENOM1", 54);

        Mockito.when(personneRepository.save(p1)).thenReturn(p1);

        Personne personneSaved = personneService.saveOrUpdate(p1);

        assertNotNull(personneSaved);
        assertEquals(p1.getId(), personneSaved.getId());
        assertEquals(p1.getNom(), personneSaved.getNom());

        verify(personneRepository).save(p1);
    }
    
    
    @Test
    public void testFindById(){
        Personne p1 = new Personne(1L, "NOM1", "PRENOM1", 34);

        Mockito.when(personneRepository.findById(p1.getId())).thenReturn(Optional.of(p1));

        Personne personneFromDb = personneService.getOne(p1.getId()).get();

        assertNotNull(personneFromDb);
        assertEquals(p1.getId(), personneFromDb.getId());

        verify(personneRepository).findById(p1.getId());
    }
    
    @Test
    public void testDelete(){

        Personne p1 = new Personne(1L, "NOM1", "PRENOM1", 34);
        
        Mockito.doNothing().when(personneRepository).deleteById(p1.getId());

        personneService.delete(p1.getId());
        
        verify(personneRepository).deleteById(1L);
    }
    
}
