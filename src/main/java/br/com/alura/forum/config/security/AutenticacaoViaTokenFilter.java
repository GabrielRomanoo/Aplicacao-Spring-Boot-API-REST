package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter { //OncePerRequestFilter eh um filtro do spring chamado uma vez a cada requisicao

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recuperarToken(request);
		System.out.println(token);
		filterChain.doFilter(request, response); //segue o fluxo da requisicao
	}

	private String recuperarToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
			return null;
		}
		
		String token = header.substring(7, header.length());
		return token;
	}

}
