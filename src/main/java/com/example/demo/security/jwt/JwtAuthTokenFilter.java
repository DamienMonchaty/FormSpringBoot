package com.example.demo.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

// La classe JwtAuthTokenFilter va filtrer les accès selon les valeurs contenues dans le jeton envoyé.
@Slf4j
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtProvider tokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	// Ce que nous faisons à l'intérieur de doFilterInternal() :

	// - obtenir JWT à partir de l'en-tête d'autorisation (en supprimant le préfixe Bearer)
	// - si le JWT est valide, on analysera le nom d'utilisateur à partir de celui-ci
	// – à partir du nom d'utilisateur, obtenez UserDetails pour créer un objet d'authentification
	// – définissez les UserDetails actuels dans SecurityContext à l'aide de la méthode setAuthentication(authentication).
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwt(request);
			if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
				String username = tokenProvider.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("can Not set User authentication -> Message : {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private String getJwt(HttpServletRequest request) {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer")) {
			return authHeader.replace("Bearer", "");
		}
		return null;
	}

}
