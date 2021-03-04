package br.com.alura.forum.controller.dto;

public class TokenDto {

	private String token;
	private String tipo; // É o tipo de autenticação a ser feita pelo cliente com o token que lhe foi devolvido

	public TokenDto(String token, String tipo) {
		this.token = token;
		this.tipo = tipo;
	}

	public String getToken() {
		return token;
	}

	public String getTipo() {
		return tipo;
	}

}
