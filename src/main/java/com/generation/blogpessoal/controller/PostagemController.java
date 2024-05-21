package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController //anotação que diz para a spring que essa é uma controladora de rotas e acesso aos métodos
@RequestMapping("/postagens") //rota para chegar nessa classe "insomnia"
@CrossOrigin(origins = "*", allowedHeaders = "*") //o origin libera o acesso a outras máquinas, o * indica isso. O allowedHeaders indica alterações no cabeçalho.
public class PostagemController {
	
	@Autowired //injeção de dependencias = instanciar a classe postagemRepository, seria um new do Scanner
	private PostagemRepository postagemRepository;
	
	@GetMapping
	ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		//findAll = SELECT * FROM tb_postagens
	}
}
