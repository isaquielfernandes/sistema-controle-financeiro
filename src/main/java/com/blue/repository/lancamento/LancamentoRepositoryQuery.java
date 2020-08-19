package com.blue.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.blue.dto.LancamentoEstatisticaCategoria;
import com.blue.dto.LancamentoEstatisticaDia;
import com.blue.dto.LancamentoEstatisticaPessoa;
import com.blue.model.Lancamento;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
	
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferente);
	
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferente);
	
	public Page<Lancamento> filtrar();
}
