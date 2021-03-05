package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

// Filtro que vai conter a lógica de recuperar o token do cabeçalho Authorization, validá-lo e autenticar o cliente, não existe anotacao para registrar um filtro, o filtro deve ser registrado via método addFilterBefore, na classe SecurityConfigurations
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter { //OncePerRequestFilter eh um filtro do spring chamado uma vez a cada requisicao

	//não da pra fazer Autowride aqui, pois essa classe nao tem nenhuma anotacao para ela, assim o spring nao controla ela
	//para driblar isso, vamos injetar a dependencia dela pelo construtor. assim, quem for usar ela, tem que injetar a classe que queremos (quem vai injetar é o SecurityConfigurations, que eh uma classe gerenciada pelo spring (por tem a anotacao @Configuration) e pode fazer o @Autowride)
	
	private TokenService tokenservice;
	
	public AutenticacaoViaTokenFilter(TokenService tokenservice) {
		this.tokenservice = tokenservice;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//recupera token
		String token = recuperarToken(request);
		
		//valida token
		boolean valido = tokenservice.isTokenValido(token);
		System.out.println(valido);
		
		//segue a requisicao
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
