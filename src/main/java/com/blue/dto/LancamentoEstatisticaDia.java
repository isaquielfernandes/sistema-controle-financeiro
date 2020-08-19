package com.blue.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.blue.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LancamentoEstatisticaDia {

	private TipoLancamento tipo;
	
	private LocalDate dia;
	
	private BigDecimal total;
}
