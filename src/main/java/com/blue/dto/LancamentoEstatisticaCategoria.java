package com.blue.dto;

import java.math.BigDecimal;

import com.blue.model.Categoria;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LancamentoEstatisticaCategoria {

	private Categoria categoria; 
	
	private BigDecimal total;
}
