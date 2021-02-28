package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
//	@ResponseBody //não precisa mais botar, o @RestController ja faz isso
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
			@RequestParam(required = true) int pagina, 
			@RequestParam(required = true) int quantidade,
			@RequestParam(required = true) String ordenacao) { //o nome do curso vem como topicos?nomeCurso=...

		Pageable paginacao = PageRequest.of(pagina, quantidade, Direction.DESC, ordenacao);
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos); 
			// O Spring faz a conversão do objeto para JSON automaticamente, com o uso da biblioteca Jackson.
		} else {
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional 
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { //o @RequestBody indica ao Spring que os parâmetros enviados no corpo da requisição devem ser atribuídos ao parâmetro do método
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//para devolver o codigo 201 http, devolvendo o tipo do objeto criado
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) { //como tem o mesmo nome, pode deixar id
		//é @PathVariable porque esta no '/', e não no '?'
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			DetalhesDoTopicoDto dto = new DetalhesDoTopicoDto(topico.get());
			return ResponseEntity.ok(dto);
		}
	
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional //avisa pro spring que é pra commitar a transacao, Métodos anotados com @Transactional serão executados dentro de um contexto transacional, Ao finalizar o método, o Spring efetuará o commit automático da transação, caso nenhuma exception tenha sido lançada.
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			TopicoDto dto = new TopicoDto(topico);
			return ResponseEntity.ok(dto); //o ok retorna 200 com o dto no body da response
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional 
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build(); //retorna 200 sem nada no body
		}
		
		return ResponseEntity.notFound().build();
	}
	
}
