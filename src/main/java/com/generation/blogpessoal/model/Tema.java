package com.generation.blogpessoal.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //entende que é uma entidade e precisa criar uma tabela
@Table(name = "tb_temas")
public class Tema {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) //padrão - estratégia de criar o id via identidade - cria o auto incremento, id = 1, id = 2...
	private Long id;
	
	@NotBlank(message = "O campo Descrição não pode estar vazio!")
	@Size(min = 4, max = 50, message = "O campo Descrição precisa ter entre 4 e 50 caracteres")
	private String descricao;
	
	//fetch é como as infos vão chegar, modo lazy = só faz se pedir
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) //cascade = remove o tema e as postagens são deletadas junto
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem; //dentro da model de tema vai ter uma lista de postagem que recebe o nome de postagns

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

}
