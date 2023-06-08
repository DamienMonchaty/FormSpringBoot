package com.example.demo.config;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.demo.models.Personne;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonneListener {
	
	@JmsListener(destination = "${pers.jms.topic}", containerFactory = "personneJmsContFactory")
	public void getPersonneListener1(Personne p) {
		log.info("Personne Listener 1 : " + p);
	}
	
	@JmsListener(destination = "${pers.jms.topic}", containerFactory = "personneJmsContFactory")
	public void getPersonneListener2(Personne p) {
		log.info("Personne Listener 2 : " + p);
	}
	
	@JmsListener(destination = "${pers.jms.topic}", containerFactory = "personneJmsContFactory")
	public void getPersonneListener3(Personne p) {
		log.info("Personne Listener 3 : " + p);
	}

}
