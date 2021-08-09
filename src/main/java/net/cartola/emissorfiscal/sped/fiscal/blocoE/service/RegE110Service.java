package net.cartola.emissorfiscal.sped.fiscal.blocoE.service;

import static net.cartola.emissorfiscal.documento.IndicadorDeOperacao.ENTRADA;
import static net.cartola.emissorfiscal.documento.IndicadorDeOperacao.SAIDA;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.documento.IndicadorDeOperacao;
import net.cartola.emissorfiscal.model.sped.fiscal.icms.propria.SpedFiscalRegE110Service;
import net.cartola.emissorfiscal.sped.fiscal.MovimentoMensalIcmsIpi;
import net.cartola.emissorfiscal.sped.fiscal.ObservacoesLancamentoFiscal;
import net.cartola.emissorfiscal.sped.fiscal.OutrasObrigacoesEAjustes;
import net.cartola.emissorfiscal.sped.fiscal.RegistroAnalitico;
import net.cartola.emissorfiscal.sped.fiscal.blocoC.RegC195;
import net.cartola.emissorfiscal.sped.fiscal.blocoC.RegC197;
import net.cartola.emissorfiscal.sped.fiscal.blocoD.RegD195;
import net.cartola.emissorfiscal.sped.fiscal.blocoD.RegD197;
import net.cartola.emissorfiscal.sped.fiscal.blocoE.RegE110;
import net.cartola.emissorfiscal.sped.fiscal.blocoE.RegE111;

/**
 * @data 11 de jun. de 2021
 * @author robson.costa
 */
@Service
class RegE110Service {
	
	@Autowired
	private RegE111Service reg111Service;
	
	@Autowired
	private SpedFiscalRegE110Service spedFiscRegE110Service;

	private Set<OutrasObrigacoesEAjustes> setOutrasObrigacoesEAjustes;

	
	public RegE110 montaGrupoRegE110(MovimentoMensalIcmsIpi movimentosIcmsIpi) {
		// TODO Auto-generated method stub
		RegE110 regE110 = new RegE110();
		
		List<RegE111> listRegE111 = reg111Service.montarGrupoRegE111(movimentosIcmsIpi);
		
		regE110.setVlTotDebitos(calcularVlTotalDebitos(movimentosIcmsIpi.getMapRegistroAnaliticoPorTipoOperacao()));	/** CAMPO 02  **/ 
		regE110.setVlAjDebitos(calcularVlAjusteDebitos(movimentosIcmsIpi.getSetObservacoesLancamentoFiscal()));			/** CAMPO 03  **/ 
		
		/**
		 *  LEMBRANDO que algumas coisas do registro E111  são preenchidas manualmente com base... ex.: estornos de devolucoes, difal etc...
		 *  mas acredito que consigo "prever a maioria desses casos, fazendo "uma pré consulta", e mostrando para o usuário antes de gerar",
		 *  acho que inclusive, isso seja viável salvar numa tabela, em que o Analista fiscal, possa editar ou até msm adicionar esses tipos de informações
		 *  antes de gerar o SPED FISCAL, 
		 */
//		(Ref.: Reg E111)		/** CAMPO 04  **/ 
//		(Ref.: Reg E111)		/** CAMPO 05  **/
		
		regE110.setVlTotCreditos(calcularVlTotalCreditos(movimentosIcmsIpi.getMapRegistroAnaliticoPorTipoOperacao()));	/** CAMPO 06  **/ 
		regE110.setVlAjCreditos(calcularVlAjusteCreditos(movimentosIcmsIpi.getSetObservacoesLancamentoFiscal()));			/** CAMPO 07  **/ 
//		movimentosIcmsIpi.getSetRegistroAnalitico().stream().forEach();
		return regE110;
	}


	// Campo 02 
	private BigDecimal calcularVlTotalDebitos(Map<IndicadorDeOperacao, Set<RegistroAnalitico>> map) {
		if (!isMapPopulado(map, SAIDA)) {
			return BigDecimal.ZERO;
		}
		Set<RegistroAnalitico> setRegistroAnalitico = map.get(SAIDA);
		BigDecimal vlTotalDebitos = setRegistroAnalitico.stream().map(RegistroAnalitico::getVlIcms).reduce(BigDecimal.ZERO, BigDecimal::add);
		return vlTotalDebitos;
	}
	
	// Campo 03
	private BigDecimal calcularVlAjusteDebitos(Set<ObservacoesLancamentoFiscal> setObservacoesLancamentoFiscal) {
		Set<OutrasObrigacoesEAjustes> setOutrasObrigacoesEAjustes = getSetOutrasObrigacoesEAjustes(setObservacoesLancamentoFiscal);
		BigDecimal vlAjusteDebitos = setOutrasObrigacoesEAjustes.stream().filter(outraObrigacao -> isValorAjusteDebitos(outraObrigacao))
				.map(OutrasObrigacoesEAjustes::getVlIcms).reduce(BigDecimal.ZERO, BigDecimal::add);
		return (vlAjusteDebitos == null) ? BigDecimal.ZERO : vlAjusteDebitos;
	}



	// Campo 06
	private BigDecimal calcularVlTotalCreditos(Map<IndicadorDeOperacao, Set<RegistroAnalitico>> map) {
		if (!isMapPopulado(map, ENTRADA)) {
			return BigDecimal.ZERO;
		}
		Set<RegistroAnalitico> setRegistroAnalitico = map.get(ENTRADA);
		BigDecimal vlTotalCreditos = setRegistroAnalitico.stream().map(RegistroAnalitico::getVlIcms).reduce(BigDecimal.ZERO, BigDecimal::add);
		return vlTotalCreditos;
	}

	// CAMPO 07
	private BigDecimal calcularVlAjusteCreditos(Set<ObservacoesLancamentoFiscal> setObservacoesLancamentoFiscal) {
		Set<OutrasObrigacoesEAjustes> setOutrasObrigacoesEAjustes = getSetOutrasObrigacoesEAjustes(setObservacoesLancamentoFiscal);
		
		BigDecimal vlAjusteCreditos = setOutrasObrigacoesEAjustes.stream().filter(outraObrigacao -> isValorAjusteCredito(outraObrigacao))
				.map(OutrasObrigacoesEAjustes::getVlIcms)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return (vlAjusteCreditos == null) ? BigDecimal.ZERO : vlAjusteCreditos;
	}

	
	/**
	 * @param setObservacoesLancamentoFiscal
	 * @return 
	 */
	private Set<OutrasObrigacoesEAjustes> getSetOutrasObrigacoesEAjustes(Set<ObservacoesLancamentoFiscal> setObservacoesLancamentoFiscal) {
		// Irei receber no metodo um set de obs. de lancamento fiscal
		
		if (this.setOutrasObrigacoesEAjustes == null || this.setOutrasObrigacoesEAjustes.isEmpty()) {
			 this.setOutrasObrigacoesEAjustes = new HashSet<>();
			
			for (ObservacoesLancamentoFiscal obsLancamentoFiscal : setObservacoesLancamentoFiscal) {
				if (obsLancamentoFiscal instanceof RegC195) {
					List<RegC197> regC197 = ((RegC195) obsLancamentoFiscal).getRegC197();
					setOutrasObrigacoesEAjustes.addAll(regC197);
				}
				
				if (obsLancamentoFiscal instanceof RegD195) {
					List<RegD197> regD197 = ((RegD195) obsLancamentoFiscal).getRegD197();
					setOutrasObrigacoesEAjustes.addAll(regD197);
				}
			}
		}
		return this.setOutrasObrigacoesEAjustes;
	}

	
	// =========================================================================== Validacoes ==============================================================================

	/**
	 * Irá verificar se tem algo no Mapa de Registro analitico, referente a uma chave (ENTRADA ou SAIDA)
	 * 
	 * @param map
	 * @param indOperacao
	 * @return
	 */
	private boolean isMapPopulado(Map<IndicadorDeOperacao, Set<RegistroAnalitico>> map, IndicadorDeOperacao indOperacao) {
		if (map == null || map.isEmpty() || !map.containsKey(indOperacao)) {
			return false;
		}
		return true;
	}
	
	// CAMPO 03
	private boolean isValorAjusteDebitos(OutrasObrigacoesEAjustes outraObrigacoes) {
		List<String> listTerceiroChar = Arrays.asList("3", "4", "5");
		List<String> listQuartoChar = Arrays.asList("0", "3", "4", "5", "6", "7", "8");
		return isValorAjusteCreditoOrDebito(outraObrigacoes, listTerceiroChar, listQuartoChar);
	}
	
	// CAMPO 06
	private boolean isValorAjusteCredito(OutrasObrigacoesEAjustes outraObrigacao) {
		List<String> listTerceiroChar = Arrays.asList("0", "1", "2");
		List<String> listQuartoChar = Arrays.asList("0", "3", "4", "5", "6", "7", "8");
		return isValorAjusteCreditoOrDebito(outraObrigacao, listTerceiroChar, listQuartoChar);
	}

	/**
	 * 
	 * @param outraObrigacoes	- Obj: Usado para escriturar os registros C197, D197 e C597 (validação será feita dentro do codAj)
	 * @param listTerceiroChar - Lista dos TERCEIRO caracteres válidos! Para o AJUSTE de CREDITO  ou DÉBITO (CAMPOS:  03 e 06) </br>
	 * @param listQuartoChar - Lista dos QUARTO caracteres válidos ! Para o AJUSTE de CREDITO  ou DÉBITO (CAMPOS:  03 e 06)
	 * @return
	 */
	private boolean isValorAjusteCreditoOrDebito(OutrasObrigacoesEAjustes outraObrigacoes, List<String> listTerceiroChar, List<String> listQuartoChar) {
		String terceiroChar = outraObrigacoes.getCodAj().substring(2, 3);
		String quartoChar = outraObrigacoes.getCodAj().substring(3, 4);
		if (listTerceiroChar.contains(terceiroChar) && listQuartoChar.contains(quartoChar)) {
			return true;
		}
		return false;
	}

	
}
