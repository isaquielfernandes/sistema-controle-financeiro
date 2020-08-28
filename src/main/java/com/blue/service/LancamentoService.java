package com.blue.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.blue.dto.LancamentoEstatisticaPessoa;
import com.blue.mail.Mailer;
import com.blue.model.Lancamento;
import com.blue.model.Pessoa;
import com.blue.model.Usuario;
import com.blue.repository.LancamentoRepository;
import com.blue.repository.PessoaRepository;
import com.blue.repository.UsuarioRepository;
import com.blue.service.exception.PessoaInexistenteOuInativoException;
import com.blue.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
		if(logger.isDebugEnabled()) {
			logger.debug("Preparando envio de e-mails de aviso de lançamento vencidos.");
		}
		
		List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		if(vencidos.isEmpty()) {
			logger.info("Sem lancamento vencidos");
			return;
		}
		
		logger.info("Existem {} lançamentos vencidos.", vencidos.size());
		
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);
		
		if(destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos, mas o sistema não encontrou destinatários.");
			return;
		}
		
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		
		logger.info("Envio de email de aviso concluido");
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> beanCollection = lancamentoRepository.porPessoa(inicio, fim);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("DT_INICIO", Date.valueOf(inicio));
		parameters.put("DT_FIM", Date.valueOf(fim));
		parameters.put("REPORT_LOCALE", new Locale("pt", "pt"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JRBeanCollectionDataSource(beanCollection));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
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
