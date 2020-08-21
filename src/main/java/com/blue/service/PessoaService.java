package com.blue.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.blue.model.Pessoa;
import com.blue.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContactos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atulizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.getContactos().clear();
		pessoaSalva.getContactos().addAll(pessoa.getContactos());
		pessoaSalva.getContactos().forEach(c -> c.setPessoa(pessoaSalva));
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
		
		return pessoaRepository.save(pessoaSalva);
	}
	
	public void atualizarPropriedade(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setActivo(true);
		pessoaRepository.save(pessoaSalva);
	}
	
	private Pessoa buscarPessoaPeloCodigo(Long codigo) {
		return pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
		
	}
	
}
