package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

@RestController //anotação que diz para a spring que essa é uma controladora de rotas e acesso aos métodos
@RequestMapping("/postagens") //rota para chegar nessa classe "insomnia"
@CrossOrigin(origins = "*", allowedHeaders = "*") //o origin libera o acesso a outras máquinas, o * indica isso. O allowedHeaders indica alterações no cabeçalho.
public class PostagemController {
	
	@Autowired //injeção de dependencias = instanciar a classe postagemRepository, seria um new do Scanner - permite que todos os metodos tenha acesso à Jpa
	private PostagemRepository postagemRepository;
	
	@GetMapping
	ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		//findAll = SELECT * FROM tb_postagens
	}
	
	//indica  uma informação variavel, localhost:8080/postagens/2 - o 2 significa o número do id que vai ser encontrado na postagem
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		//findById = select * from tb_postagens WHERE id =1; mapeamento
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) 
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//SELECT * FROM tb_postagens WHERE titulo = "titulo";
	@GetMapping("/titulo/{titulo}") //consulta no insomnia: localhost:8080/postagens/titulo - Primeiro entra a palavra fixa que é o titulo e depois a variavel
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		
	}
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		
		return postagemRepository.findById(postagem.getId())
				.map(resposta-> ResponseEntity.status(HttpStatus.OK)
				.body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
	}
	
	//Get - recuperar as infos do banco de dados 
	//Post - inserir no banco de dados 
	//Put - update no banco de dados 
	//Delete - deleta algum registro no banco de dados 
}
