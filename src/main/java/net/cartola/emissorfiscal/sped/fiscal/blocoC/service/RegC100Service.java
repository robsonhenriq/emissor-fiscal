package net.cartola.emissorfiscal.sped.fiscal.blocoC.service;

import static java.util.stream.Collectors.toList;
import static net.cartola.emissorfiscal.util.SpedFiscalUtil.getCodSituacao;
import static net.cartola.emissorfiscal.util.SpedFiscalUtil.getIndicadorEmitente;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.documento.DocumentoFiscal;
import net.cartola.emissorfiscal.documento.IndicadorDeOperacao;
import net.cartola.emissorfiscal.documento.NFeStatus;
import net.cartola.emissorfiscal.loja.Loja;
import net.cartola.emissorfiscal.sped.fiscal.MontaGrupoDeRegistroList;
import net.cartola.emissorfiscal.sped.fiscal.MovimentoMensalIcmsIpi;
import net.cartola.emissorfiscal.sped.fiscal.blocoC.RegC100;
import net.cartola.emissorfiscal.sped.fiscal.enums.ModeloDocumentoFiscal;

/**
 * 18/09/2020
 * @author robson.costa
 */
@Service
class RegC100Service implements MontaGrupoDeRegistroList<RegC100, MovimentoMensalIcmsIpi> {

	private static final Logger LOG = Logger.getLogger(RegC100Service.class.getName());

	
	@Autowired
	private RegC110Service regC110Service;
	
	@Autowired
	private RegC120Service regC120Service;
	
	@Autowired
	private RegC160Service regC160Service;
	
	@Autowired
	private RegC165Service regC165Service;
	
	@Autowired
	private RegC170Service regC170Service;
	
//	@Autowired
//	private RegC170Service regC170Service;
	
	@Autowired
	private RegC190Service regC190Service;

	@Autowired
	private RegC195Service regC195Service;	

	
	@Override
	public List<RegC100> montarGrupoDeRegistro(MovimentoMensalIcmsIpi movimentosIcmsIpi) {
		LOG.log(Level.INFO, "Montando o Registro C100");
		
		Loja lojaSped = movimentosIcmsIpi.getLoja();
		List<DocumentoFiscal> listDocFiscalEntradaEmissaoTerceiros = getDocFiscalEntradaEmissaoTerceiros(movimentosIcmsIpi);
		List<DocumentoFiscal> listDocFiscalEmissaoPropria = getDocFiscalEmissaoPropria(movimentosIcmsIpi);
		
		List<RegC100> listRegC100 = new ArrayList<>();

		/**
		 * Todos os DocumentoFiscal de entrada, emitidos por terceiros e que NÃO sejam modelo 65 (NFC-e)
		 */
		listDocFiscalEntradaEmissaoTerceiros.stream().forEach(docFiscEntradaEmissTerceiros -> {
			listRegC100.add(preecheRegC100EntradaEmissaoTerceiros(docFiscEntradaEmissTerceiros, lojaSped));
		});
		
		/**
		 * TODO -> AINDA NÃO CHEGUEI NESSE PREENCHIMENTO, ao enos não totalmente. POIS no método
		 * preecheRegC100EntradaEmissaoTerceiros(...), tem uma validação para quando emitimos, então talvez esse caso preencimento abaixo
		 * não seja necessário
		 */
		listDocFiscalEmissaoPropria.stream().forEach(docFiscEmissaoPropria -> {
//			listRegC100.add(preencheRegC100EmissaoPropria(docFiscEmissaoPropria, lojaSped));
		});
		
		LOG.log(Level.INFO, "Registro C100, terminado. REG C100: {0} " ,listRegC100);
		return listRegC100;
	}


	
	/**
	 * Irá escriturar todas NFES de entrada (Todas que sejam ENTRADA e emitidas por TERCEIROS), conforme as regras do Guia de Preenchimento. 
	 * (CASO NÃO se encaixe em nenhuma das DEZ EXCECOES do GUIA, a NFE será escriturada NORMAL: REG C100, C170 e C190)
	 * 
	 * @param docFisc
	 * @param lojaSped
	 * @return
	 */
	private RegC100 preecheRegC100EntradaEmissaoTerceiros(DocumentoFiscal docFisc, Loja lojaSped) {
		// TODO Auto-generated method stub
		LOG.log(Level.INFO, "Montando o Registro C100 ");
		RegC100 regC100 = new RegC100();
		
		/** PS: Por enquanto só tem a validação do PRIMEIRO preenchimento **/
		TipoPreenchimentoRegC100 tipoPreenchimentoRegC100 = verificaTipoPreenchimento(docFisc, lojaSped);
		
		switch (tipoPreenchimentoRegC100) {
		case EX_1_COD_SITUACAO:
			regC100 = prencheC100ExcecaoUmCodSit(docFisc, lojaSped);
			break;
		case EX_2_NFE_EMISSAO_PROPRIA:				// AQUI já ESTARÁ as ENTRADAS E SAÍDAS EMITITDAS PELAS AUTOGERAL, Então NÂO PRECISO iterar a LISTA EM DOIS METODOS DIFERENTES
			regC100 = preencheC100ExcecaoDoisEmissaoPropria(docFisc, lojaSped);
			break;

		case EX_3_NFE_COMPLEMENTAR:
			
			break;
		case EX_4_NFE_REGIME_ESPECIAL_NORMA_ESPECIFICA:
			
			break;
		case EX_5_:
			
			break;
		case EX_6_VENDA_COM_RESSARCIMENTO:
			
			break;
		case EX_7_NFE_EMITIDA_TERCEIROS:
			
			break;
		case EX_8_NFE_UF_CONSUMO_PREENCHIDO:
			
			break;
		case EX_9_NFCE:
			
			break;
		case EX_10_INFO_COMPLEMENTAR_ITEM:
			
			break;
		
		default:
			/** Se não for nenhum dos casos acima en tão é o preenchimento normal **/
			regC100 = new RegC100(docFisc, lojaSped);
			regC100.setRegC170(regC170Service.montarGrupoRegC170(docFisc));
			regC100.setRegC190(regC190Service.montarGrupoRegC190(docFisc));
			regC100.setRegC195(regC195Service.montarGrupoRegC195(docFisc));
			break;
		}
			
		
		return regC100;
	}


	
	/**
	 * Dado um DocumentoFiscal, irá validar qual é o "tipo de preenchimento" (Se o mesmo se encaixa em alguma das DEZ EXCECOES, da Documentacao, GUIA PRATICO EFD).
	 * 
	 * Caso não se encaixe o {@link TipoPreenchimentoRegC100} então é "NORMAL"
	 * 
	 * @param docFisc
	 * @param lojaSped
	 * @return
	 */
	private TipoPreenchimentoRegC100 verificaTipoPreenchimento(DocumentoFiscal docFisc, Loja lojaSped) {
		LOG.log(Level.INFO, "Verificando o tipo de preenchimento do Registro C100");

		List<NFeStatus> nfesNaoAutorizadas = Arrays.asList(NFeStatus.CANCELADA, NFeStatus.DENEGADA, NFeStatus.INUTILIZADA);
		
		if (nfesNaoAutorizadas.contains(docFisc.getStatus())) {
			return TipoPreenchimentoRegC100.EX_1_COD_SITUACAO;
		}
		
		if (docFisc.getEmitente().getCnpj().equals(lojaSped.getCnpj())) {
			return TipoPreenchimentoRegC100.EX_2_NFE_EMISSAO_PROPRIA;
		}
		
		
		if (true) {
			return TipoPreenchimentoRegC100.EX_3_NFE_COMPLEMENTAR;
		}
		
		
		/** PS: Por enquanto só tem a validação do PRIMEIRO preenchimento **/
		return TipoPreenchimentoRegC100.NORMAL;
	}



	/**
	 * Irá preencher o REG C100 - Caso se encaixe na EXCEÇÃO 1, do Guia Pratico da EFD, que diz basicamente o Seguinte:
	 * "...DocumentosFiscais com o CODIGO SITUACAO 2,3,4 (Cancelado, Cancelado extemporêno e Denegado), Preencha somente --> REG, IND_OPER, IND_EMIT, COD_MOD, COD_SIT, SER, NUM_DOC e CHV_NF-e..." 
	 * "...Para os com CODIGO SITUACAO 5 (Numercao Inutilizada), todos os campos anteriores. EXCETO o campo CHV_NF-e...."
	 * 
	 * Demais campos deverão ser apresentados com conteúdo VAZIO “||”. Não informar registros filhos. 
	 * 
	 * @param docFisc
	 * @param lojaSped
	 * @return RegC100 preenchido na regra -> {@linkplain TipoPreenchimentoRegC100}.EX_1_COD_SITUACAO
	 */
	private RegC100 prencheC100ExcecaoUmCodSit(DocumentoFiscal docFisc, Loja lojaSped) {
		RegC100 regC100 = new RegC100();
		
		regC100.setIndOper(docFisc.getTipoOperacao());
		regC100.setIndEmit(getIndicadorEmitente(docFisc, lojaSped));
		regC100.setCodMod(docFisc.getModelo());
		regC100.setCodSit(getCodSituacao(docFisc));
		regC100.setSer(docFisc.getSerie());
		regC100.setNumDoc(docFisc.getNumeroNota());
		regC100.setChvNfe(docFisc.getNfeChaveAcesso());
		// ACHO QUE NEM PRECISA DISSO
//		if (docFisc.getStatus().equals(NFeStatus.INUTILIZADA)) {
//			regC100.setChvNfe("");
//		}
		
		return regC100;
	}

	
	/**
	 * NFe Emissão própria - Regra Geral - Preencher somente os REGISTROS C100 e C190, 
	 * SE existir ajuste de DoumentoFiscal preencha ->> C195 e C197
	 * C170 - Somente preencha o C170 quando tiver que informar os registros - C176, C180 C181 ou o C177 (No caso de haver info. complementar do Item | Tabela 5.6) 
	 * C110, C120, C185 e C186 - A critério de cada UF
	 * C101 - Informar nas operações Interestaduais para Consumidor final Não Contribuinte do ICMS
	 * 
	 * @param docFisc
	 * @param lojaSped
	 * @return
	 */
	private RegC100 preencheC100ExcecaoDoisEmissaoPropria(DocumentoFiscal docFisc, Loja lojaSped) {
		// TODO Auto-generated method stub
		/**
		 * C100 e C190
		 * 
		 */
		RegC100 regC100 = new RegC100(docFisc, lojaSped);
		
		regC100.setRegC190(regC190Service.montarGrupoRegC190(docFisc));
		
		
		return regC100;
	}

	
	
	
	// ========================================================================================================================================================================
	// ========================================================================================================================================================================
	// ========================================================================================================================================================================
	// ========================================================================================================================================================================

	
	
	
	/**
	 * Irá Retornar TODOS os DocumentoFiscais que sejam de ENTRADA. Porém que NÃO sejam, de EMISSÃO PRÓPRIA (Ex.: Operacao: Devolução do Cliente, que nós emitimos a NFE)
	 * Ou seja, Apenas ENTRADA, cuja a EMISSAO seja de TERCEIROS (cnpjEmitente != cnpjLojaSpedFiscal)
	 * 
	 * @param movimentosIcmsIpi
	 * @return List<DocumentoFiscal> - Todos DocumentoFiscais, de entrada, cujo o emitente != lojaSped
	 */
	private List<DocumentoFiscal> getDocFiscalEntradaEmissaoTerceiros(MovimentoMensalIcmsIpi movimentosIcmsIpi) {
		List<DocumentoFiscal> listDocumentoFiscal = movimentosIcmsIpi.getListDocumentoFiscal();
		Loja lojaSped = movimentosIcmsIpi.getLoja();
		
		List<DocumentoFiscal> listDocFiscEntrada = listDocumentoFiscal.stream()
			.filter(docFisc -> docFisc.getTipoOperacao() == IndicadorDeOperacao.ENTRADA && getModelosDocFiscRegC100().contains(docFisc.getModelo())
			&& !docFisc.getEmitente().getCnpj().equalsIgnoreCase(lojaSped.getCnpj()))
			.collect(toList());
		
		return listDocFiscEntrada;
	}

	
	/**
	 * Irá retornar TODOS os DocumentosFiscais de EMISSAO Propria.:
	 * 
	 * <b> Saída --> </b> TODOS os DocumentoFiscais, que emitimos
	 * <b> Entrada --> </b> SOMENTE os que nós emitimos. Ou seja, cnpjEmitente == cnpjLojaSpedFiscal
	 * 
	 * PS: NFCe (Modelo 65), Serve somente para as Operações de Venda. (Caso vá fazer uma Devolução dela, é criado Uma NFE (Modelo 55) que refencia a NFCe.
	 * 
	 * @param movimentosIcmsIpi
	 * @return List<DocumentoFiscal> - Lista de Todos DocumentosFiscais, que devem ser escriturados na Saída
	 */
	private List<DocumentoFiscal> getDocFiscalEmissaoPropria(MovimentoMensalIcmsIpi movimentosIcmsIpi) {
		List<DocumentoFiscal> listDocumentoFiscal = movimentosIcmsIpi.getListDocumentoFiscal();
		Loja lojaSpedFiscal = movimentosIcmsIpi.getLoja();
		
		List<ModeloDocumentoFiscal> modelosDocFiscSaida = getModelosDocFiscRegC100();
		modelosDocFiscSaida.add(ModeloDocumentoFiscal._65);
		
		List<DocumentoFiscal> listDocFiscSaida = listDocumentoFiscal.stream()
				.filter(docFisc -> docFisc.getTipoOperacao() == IndicadorDeOperacao.SAIDA && modelosDocFiscSaida.contains(docFisc.getModelo()) 
				|| (docFisc.getTipoOperacao() == IndicadorDeOperacao.ENTRADA && getModelosDocFiscRegC100().contains(docFisc.getModelo()) 
					&& docFisc.getEmitente().getCnpj().equalsIgnoreCase(lojaSpedFiscal.getCnpj())))
				.collect(toList());
		
		return listDocFiscSaida;
	}

	
	/**
	 * Obtem Todos os Modelos de Documentos Fiscais de ENTRADA e SAIDA, que devem ser escriturados na REGISTRO C100.
	 * 
	 * PS: Modelo 65 -> Somente deve ser escriturado na saída, por isso não foi adicionado aqui
	 * 
	 * @return List<ModeloDocumentoFiscal> 
	 */
	private List<ModeloDocumentoFiscal> getModelosDocFiscRegC100() {
		List<ModeloDocumentoFiscal> listModeloDocFisc = new ArrayList<>();
		
		listModeloDocFisc.add(ModeloDocumentoFiscal._1);
		listModeloDocFisc.add(ModeloDocumentoFiscal._1B);
		listModeloDocFisc.add(ModeloDocumentoFiscal._4);
		listModeloDocFisc.add(ModeloDocumentoFiscal._55);
		
		return listModeloDocFisc;
	}
	
	
}
