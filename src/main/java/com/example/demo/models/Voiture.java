package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Voiture {
	
	private Long id;
	@NonNull
	private String marque;
	@NonNull
	private String modele;
	@NonNull
	private String couleur;
}
