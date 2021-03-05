package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration //o spring irá ler as configuracoes que estiverem dentro dessa classe
public class SecurityConfigurations extends WebSecurityConfigurerAdapter { 
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenservice;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean //com o @Bean, o spring sabe que este metodo devolve o AuthenticationManager para ser usado na injecao de dependencias (duvida respondida aqui: https://cursos.alura.com.br/forum/topico-bean-145870#919742)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager(); //como tem o super, este metodo vem da classe pai, da classe que estamos herdando (extends)
	}
	
	//Configuracoes de Autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService) //uerDetailsService diz para o spring qual é a classe que tem a logica de autenticacao
			.passwordEncoder(new BCryptPasswordEncoder());
	}		
	
	//Configuracoes de Autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll() //listar topicos
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll() //detalhar topicos
			.antMatchers(HttpMethod.POST, "/auth").permitAll() //autenticacao usuario
			.anyRequest().authenticated() //qualquer outra requisicao(url) precisa estar autenticada
		.and() //AUTENTICACAO MODO WEB TOKEN
			.csrf().disable() //desabilitamos porque nossa aplicação ja esta livre do tipo de ataque csrf, porque estamos usando autenticacao via token
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //avisamos pro spring que quando fizer autenticacao, nao eh pra criar sessao, porque vamos usar token (autenticacao de maneira stateless)
		.and()
			.addFilterBefore(new AutenticacaoViaTokenFilter(tokenservice, usuarioRepository), UsernamePasswordAuthenticationFilter.class); //adiciona o nosso filtro (interceptador) pra ser rodado antes do filtro de autenticacao do spring
		
		/* AUTENTICACAO MODO SESSION 
		.and()
			.formLogin(); //fala pro spring gerar um formulario de autenticacao e um controller que recebe as requisicoes desse formualario
		*/
	}
	
	//Configuracoes de recursos estaticos (js, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
}
