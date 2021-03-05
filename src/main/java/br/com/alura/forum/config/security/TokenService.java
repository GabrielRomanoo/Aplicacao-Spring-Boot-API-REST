package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service //é para indicar ao Spring que a classe deve ser gerenciada por ele, e assim conseguimos a injetar em outras classes, como nos controllers.
public class TokenService {
	
	//injetando uma propriedade, um parametro do aplication.proprieties
	@Value("${forum.jwt.expiration}") //nao usa o @Autowired, porque o @Autowired procura um bean que ta configurado
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		//utilizando a api do jjwt (importada nas dependencias do pom.xml)
		return Jwts.builder() //builder para setar as infos para construir o token
					.setIssuer("API do Fórum da Alura") //setar quem esta gerando o token (quem é a aplicacao que fez a geracao do token)
					.setSubject(logado.getId().toString()) //setar quem eh o usuario, quem eh o dono do token, quem eh o usuario autenticado que este token pertence
					.setIssuedAt(hoje) //diz qual foi a data de geracao deste token
					.setExpiration(dataExpiracao) //diz qual eh a data de expiracao do token
					.signWith(SignatureAlgorithm.HS256, secret) //diz quem é o algoritmo de criptografia do token, e qual é a senha da aplicacao (senha usada para gerar o hash da criptografia do token)
					.compact(); //compacta as infos anteriores e transforma em uma string
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser() //descripitografar o token
			.setSigningKey(this.secret) //chvave para descripitografar
			.parseClaimsJws(token); //recupera o token e as informacoes setadas dentro dele no metodo gerarToken acima
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		String subject = claims.getSubject();
		Long id = Long.valueOf(subject);
		return id;
	}

}
