package net.cartola.emissorfiscal.tributacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import net.cartola.emissorfiscal.loja.Loja;
import net.cartola.emissorfiscal.tributacao.estadual.TipoGuia;

/**
 * Deverá calcular o valor que tem que pagar para cada item, de ICMS ST, nas aquisicoes vindas de 
 * ... SC, MG e ES ...
 * 		
 * @date 8 de mar. de 2021
 * @author robson.costa
 */
@Getter
@Setter
public class CalculoGareCompras implements Serializable {
	
	private static final long serialVersionUID = 1552631177897343928L;
	
	private TipoGuia tipoGuia;
	private int codigoReceita;
	private Loja loja;
	private String infoComplementar;
	private LocalDate dataVencimento;
	private String mesAnoReferencia;
	private BigDecimal valorPrincipal;
	private BigDecimal juros;
	private BigDecimal multa;
	private BigDecimal total;
	
}
