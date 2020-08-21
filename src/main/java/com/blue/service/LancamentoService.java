package com.blue.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.blue.mail.Mailer;
import com.blue.model.Lancamento;
import com.blue.model.Pessoa;
import com.blue.repository.LancamentoRepository;
import com.blue.repository.PessoaRepository;
import com.blue.repository.UsuarioRepository;
import com.blue.service.exception.PessoaInexistenteOuInativoException;
import com.blue.storage.S3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "ROLE_PESQUIZAR_LANCAMENTO";
	private static Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private S3 s3;
	
	@Autowired
	private Mailer mailer;
	
	
	
	public Lancamento salvar(Lancamento lancamento) {
		validarPessoa(lancamento);
		
		if(StringUtils.hasText(lancamento.getAnexo()))
			s3.salvar(lancamento.getAnexo());
		
		return lancamentoRepository.save(lancamento);
	}
	
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa()))
			validarPessoa(lancamento);
		
		if(StringUtils.isEmpty(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	public void avisarSobreLancamentoVencidos() {
		
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		
		return null;
	}
	
	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		
		if(lancamento.getPessoa().getCodigo() != null) 
			pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).get();
		
		if(pessoa == null || pessoa.isInativo())
			throw new PessoaInexistenteOuInativoException();
		
	}
	
	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException());
		
		return lancamentoSalvo;
	}
}
