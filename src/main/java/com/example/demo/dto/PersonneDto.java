package com.example.demo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonneDto {

    @Size(min = 2, max = 15, message = "Le nom doit faire min 2 caracteres max 15")
    @NotBlank
	private String nom;
    @Size(min = 2, max = 15, message = "Le nom doit faire min 2 caracteres max 15")
    @NotBlank
	private String prenom;
    @Min(value = 18, message = "L'âge doit être supérier à 18")
    @NotNull
	private Integer age;
}
