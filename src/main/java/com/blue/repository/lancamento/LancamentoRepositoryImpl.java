package com.blue.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.blue.dto.LancamentoEstatisticaCategoria;
import com.blue.dto.LancamentoEstatisticaDia;
import com.blue.dto.LancamentoEstatisticaPessoa;
import com.blue.model.Lancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@Override
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Lancamento> filtrar() {
		// TODO Auto-generated method stub
		return null;
	}

}
