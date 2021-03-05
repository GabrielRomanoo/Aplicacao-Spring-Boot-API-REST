package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	@Autowired // o spring nao faz a injecao de dependencias desta classe automaticamente, foi preciso configurar o @Bean dela no SecurityConfigurations
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
		// precisamos fazer a autenticacao na mao, porque estamos usando stateless, e nao mais session

		UsernamePasswordAuthenticationToken dadosLogin = form.converte(); //transformamos os dados de login em um objeto

		try {
			/* o authManager dispara a logica de autenticacao */
			Authentication authentication = authManager.authenticate(dadosLogin); //quando cair nessa linha, o spring vai olhar o SecurityConfigurations e ver a configuracao de autenticação (e ele sabe que é pra usar o autenticacaoService)

			/* geracao do token abaixo JWT (utilizando a biblioteca jjwt)*/
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer")); //devolve 200
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build(); //devolve erro 400
		}
	}

}
