package com.example.demo.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SigninDto;
import com.example.demo.dto.SignupDto;
import com.example.demo.models.Role;
import com.example.demo.models.RoleName;
import com.example.demo.models.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtProvider;

import springfox.documentation.service.ResponseMessage;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	// – /api/auth/signin
	//
	// authentifier { nom d'utilisateur, mot de passe }
	// mettre à jour SecurityContext à l'aide de l'objet Authentication
	// générer JWT
	// obtenir les détails de l'utilisateur à partir de l'objet d'authentification
	// la réponse contient des données JWT et UserDetails

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninDto signinDto) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signinDto.getUsername(), signinDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);

		// UserDetails user = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(jwt);
        // return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.getAuthorities()));
	}

	// – /api/auth/signup
	//
	// créer un nouvel utilisateur (avec ROLE_USER si le rôle n'est pas spécifié)
	// enregistrer l'utilisateur dans la base de données à l'aide de UserRepository

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpDto) {

		User user = new User(signUpDto.getUsername(), encoder.encode(signUpDto.getPassword()));

		Set<String> strRoles = signUpDto.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByTitle(RoleName.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Fail! -> Cause : User role not find"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByTitle(RoleName.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause : User role not find"));
					roles.add(adminRole);
				default:
					Role userRole = roleRepository.findByTitle(RoleName.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause : User role not find"));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>("User Registered successfully", HttpStatus.OK);

	}
}
