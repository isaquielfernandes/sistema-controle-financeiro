package com.blue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "contacto")
public class Contacto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotEmpty
	private String nome;
	
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String telefone;
	
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
}
