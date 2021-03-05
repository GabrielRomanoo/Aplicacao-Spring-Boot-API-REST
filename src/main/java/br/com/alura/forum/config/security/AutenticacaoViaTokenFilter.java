package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

// Filtro que vai conter a lógica de recuperar o token do cabeçalho Authorization, validá-lo e autenticar o cliente, não existe anotacao para registrar um filtro, o filtro deve ser registrado via método addFilterBefore, na classe SecurityConfigurations
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter { //OncePerRequestFilter eh um filtro do spring chamado uma vez a cada requisicao

	//não da pra fazer Autowride aqui, pois essa classe nao tem nenhuma anotacao para ela, assim o spring nao controla ela
	//para driblar isso, vamos injetar a dependencia dela pelo construtor. assim, quem for usar ela, tem que injetar a classe que queremos (quem vai injetar é o SecurityConfigurations, que eh uma classe gerenciada pelo spring (por tem a anotacao @Configuration) e pode fazer o @Autowride)
	
	private TokenService tokenservice;
	
	private UsuarioRepository usuarioRepository;
	
	public AutenticacaoViaTokenFilter(TokenService tokenservice, UsuarioRepository usuarioRepository) {
		this.tokenservice = tokenservice;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//recupera token
		String token = recuperarToken(request);
		
		//valida token
		boolean valido = tokenservice.isTokenValido(token);
		
		if (valido) {
			autenticarCliente(token);
		}
		//se nao tiver valido, o filterChain vai seguir com a requisicao e o spring vai barrar, por nao estar autenticado
		
		//segue a requisicao
		filterChain.doFilter(request, response); //segue o fluxo da requisicao
	}

	private void autenticarCliente(String token) {
		//buscando a identificacao do usuario dentro do token
		Long idUsuario = tokenservice.getIdUsuario(token);
		
		//procurando usuario no banco
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		
		//forçando a autenticacao do usuario
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
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
