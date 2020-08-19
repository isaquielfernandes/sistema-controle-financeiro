package com.blue.dto;

import java.math.BigDecimal;

import com.blue.model.Pessoa;
import com.blue.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LancamentoEstatisticaPessoa {

	private TipoLancamento tipo;
	
	private Pessoa pessoa;
	
	private BigDecimal total;
}
