package com.blue.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blue.dto.LancamentoEstatisticaCategoria;
import com.blue.dto.LancamentoEstatisticaDia;
import com.blue.dto.LancamentoEstatisticaPessoa;
import com.blue.model.Lancamento;
import com.blue.repository.filter.LancamentoFilter;
import com.blue.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
	
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferente);
	
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferente);
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable);
}
