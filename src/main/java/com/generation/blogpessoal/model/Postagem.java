package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //classe vai se tornar uma entidade(tabela) do banco de dados
@Table(name="tb_postagens") //nomeando a tabela no banco de dados 
public class Postagem {
	
	@Id //cada atributo da model se torna uma coluna na tabela, id torna o campo uma chave primaria no BD
	@GeneratedValue(strategy=GenerationType.IDENTITY) //tornando a chave primária em auto increment
	private Long id;
	
	@NotBlank(message = "O atributo TÍTULO é obrigatório!") //validation - validar nosso atributo NN e não vazio
	@Size(min = 5, max = 100, message= "O atributo TÍTULO deve ter no mínimo 5 caracteres e no máximo 100 caracteres.")
	private String titulo;
	
	@NotBlank(message= "O atributo TEXTO é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo TEXTO deve ter no mínimo 10 caracteres e no máximo 1000 caracteres.")
	private String texto;
	
	@UpdateTimestamp //criando configuração de data igual de uma rede social, no momento que a postagem entrou no banco de dados ele preenche automático
	private LocalDateTime data;
	
	@ManyToOne // significa que pode ter muitas postagens para um tema
	@JsonIgnoreProperties("postagem") //evitar loop infinito
	private Tema tema; //criando dado como privado, podendo ter acesso apenas pelo getters and setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
}
