package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.models.Personne;
import com.example.demo.services.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonneRestController.class)
public class PersonneRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IService<Personne> personneService;

	@Test
	public void TestGetPersonnes() throws Exception {
		Personne p1 = new Personne("Wick", "John", 40);
		Personne p2 = new Personne("Doe", "Bob", 30);

		List<Personne> personnes = Arrays.asList(p1, p2);

		Mockito.when(personneService.findAll()).thenReturn(personnes);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/personnes").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expectedJson = this.mapToJson(personnes);
		String outputInJson = result.getResponse().getContentAsString();

		assertThat(outputInJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void testGetPersonneById() throws Exception {

		Personne mockPersonne = new Personne(1L, "admin", "admin", 10);

		Mockito.when(personneService.getOne(mockPersonne.getId())).thenReturn(Optional.of(mockPersonne));
		
		String URI = "/personnes/1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		String expectedJson = this.mapToJson(mockPersonne);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
	}
	
	@Test
	public void testSave() throws Exception {

		Personne mockPersonne = new Personne(1L, "admin", "admin", 19);

		String inputInJson = this.mapToJson(mockPersonne);

		String URI = "/personnes";

		Mockito.when(personneService.saveOrUpdate(Mockito.any(Personne.class))).thenReturn(mockPersonne);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI).accept(MediaType.APPLICATION_JSON)
				.content(inputInJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String outputInJson = response.getContentAsString();

		assertThat(outputInJson).isEqualTo(inputInJson);

	}

	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

}
