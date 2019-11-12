package net.cartola.emissorfiscal.emissorfiscal.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.cartola.emissorfiscal.documento.DocumentoFiscal;
import net.cartola.emissorfiscal.documento.DocumentoFiscalItem;
import net.cartola.emissorfiscal.documento.DocumentoFiscalItemRepository;
import net.cartola.emissorfiscal.documento.DocumentoFiscalRepository;
import net.cartola.emissorfiscal.documento.Finalidade;
import net.cartola.emissorfiscal.documento.Pessoa;
import net.cartola.emissorfiscal.estado.Estado;
import net.cartola.emissorfiscal.estado.EstadoRepository;
import net.cartola.emissorfiscal.estado.EstadoSigla;
import net.cartola.emissorfiscal.ncm.Ncm;
import net.cartola.emissorfiscal.ncm.NcmRepository;
import net.cartola.emissorfiscal.operacao.Operacao;
import net.cartola.emissorfiscal.operacao.OperacaoRepository;
import net.cartola.emissorfiscal.tributacao.estadual.TributacaoEstadualRepository;
import net.cartola.emissorfiscal.tributacao.federal.TributacaoFederalRepository;

/**
 * 8 de nov de 2019
 * 
 * @author gregory.feijon
 */

@Component
public class TestHelper {

	private static final String NCM1 = Integer.toString(NcmServiceLogicTest.NCM_NUMERO_REGISTRO_1);
	private static final String NCM2 = Integer.toString(NcmServiceLogicTest.NCM_NUMERO_REGISTRO_2);
	private static final String NCM3 = "34561287";

	public static String OPERACAO_VENDA = "Venda";
	public static String OPERACAO_VENDA_INTERESTADUAL = "Venda Interestadual";
	public static String OPERACAO_COMPRA = "Compra";
	public static String OPERACAO_DEVOLUÇAO = "Devolução";
	public static String OPERACAO_DEVOLUCAO_FORNECEDOR = "Devolução para o fornecedor";
	public static String OPERACAO_DEVOLUCAO_FORNECEDOR_FORA_ESTADO = "Devolução para o fornecedor fora do estado";
	public static String OPERACAO_DEVOLUCAO_CLIENTE = "Devolução do cliente";
	public static String OPERACAO_REMESSA = "Remessa";
	public static String OPERACAO_REMESSA_CONSIGNADA = "Remessa consignada";

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private OperacaoRepository operacaoRepository;

	@Autowired
	private NcmRepository ncmRepository;

	@Autowired
	private DocumentoFiscalRepository docFiscalRepository;

	@Autowired
	private DocumentoFiscalItemRepository docFiscalItemRepository;

	@Autowired
	private TributacaoEstadualRepository tributacaoEstadualRepository;

	@Autowired
	private TributacaoFederalRepository tributacaoFederalRepository;

	public void criarEstados() {
		List<Estado> estados = new LinkedList<>();
		String[][] data = { { "SP", "São Paulo" }, { "MG", "Minas Gerais" }, { "RS", "Rio Grande do Sul" },
				{ "PR", "Paraná" } };

		for (String[] dados : data) {
			int aux = 0;
			Estado estado = new Estado();
			estado.setSigla(EstadoSigla.valueOf(dados[aux++]));
			estado.setNome(dados[aux++]);
			estados.add(estado);
		}
		estadoRepository.saveAll(estados);
	}

	private List<Operacao> defineOperacoes() {
		List<Operacao> operacoes = new LinkedList<>();
		String[][] data = { { OPERACAO_VENDA }, { OPERACAO_VENDA_INTERESTADUAL }, { OPERACAO_COMPRA },
				{ OPERACAO_DEVOLUÇAO }, { OPERACAO_DEVOLUCAO_FORNECEDOR },
				{ OPERACAO_DEVOLUCAO_FORNECEDOR_FORA_ESTADO }, { OPERACAO_DEVOLUCAO_CLIENTE }, { OPERACAO_REMESSA },
				{ OPERACAO_REMESSA_CONSIGNADA } };

		for (String[] dados : data) {
			int aux = 0;
			Operacao operacao = new Operacao();
			operacao.setDescricao(dados[aux]);
			operacoes.add(operacao);
		}
		operacaoRepository.saveAll(operacoes);
		return operacoes;
	}

	private List<Ncm> defineNcms() {
		List<Ncm> ncms = new LinkedList<>();
		String[][] data = { { NCM1, "43", "Essa é uma DESCRIÇÃO do PRIMEIRO NCM para o teste" },
				{ NCM2, "32", "Essa é uma DESCRIÇÃO do SEGUNDO NCM para o teste" },
				{ NCM3, "54", "Essa é uma DESCRIÇÃO do TERCEIRO NCM para o teste" } };

		for (String[] dados : data) {
			int aux = 0;
			Ncm ncm = new Ncm();
			ncm.setNumero(Integer.parseInt(dados[aux++]));
			ncm.setExcecao(Integer.parseInt(dados[aux++]));
			ncm.setDescricao(dados[aux++]);
			ncms.add(ncm);
		}
		ncmRepository.saveAll(ncms);
		return ncms;
	}

	public void criarDocumentoFiscal() {
		List<DocumentoFiscal> documentosFiscais = new LinkedList<>();
		List<Operacao> operacoes = defineOperacoes();
		List<Ncm> ncms = defineNcms();
		List<DocumentoFiscalItem> itens = criarDocumentoFiscalItem(ncms);

		String[][] data = { { "tipo1", "SP", "Emitente Regime Apuração 1", "SP", "FISICA", OPERACAO_VENDA },
				{ "tipo2", "SP", "Emitente Regime Apuração 2", "SP", "JURIDICA", OPERACAO_VENDA },
				{ "tipo3", "SP", "Emitente Regime Apuração 3", "MG", "FISICA", OPERACAO_VENDA },
				{ "tipo4", "SP", "Emitente Regime Apuração 4", "MG", "JURIDICA", OPERACAO_VENDA } };

		for (String[] dados : data) {
			int aux = 0;
			DocumentoFiscal docFiscal = new DocumentoFiscal();
			docFiscal.setTipo(dados[aux++]);
			docFiscal.setEmitenteUf(EstadoSigla.valueOf(dados[aux++]));
			docFiscal.setEmitenteRegimeApuracao(dados[aux++]);
			docFiscal.setDestinatarioUf(EstadoSigla.valueOf(dados[aux++]));
			docFiscal.setDestinatarioPessoa(Pessoa.valueOf(dados[aux++]));
			String operacaoDescricao = dados[aux++];
			docFiscal.setOperacao(operacoes.stream()
					.filter(operacao -> operacao.getDescricao().equals(operacaoDescricao)).findAny().get());
			docFiscal.setItens(itens);
			documentosFiscais.add(docFiscal);
		}
		docFiscalRepository.saveAll(documentosFiscais);
//		documentosFiscais.stream().forEach(docFiscal -> {
//			docFiscalItemRepository.saveAll(docFiscal.getItens());
//		});
	}

	private List<DocumentoFiscalItem> criarDocumentoFiscalItem(List<Ncm> ncms) {
		List<DocumentoFiscalItem> documentoFiscalItens = new LinkedList<>();

		String[][] data = { { "CONSUMO", "10", "5", "5506", NCM1 }, { "CONSUMO", "5", "5", "5506", NCM2 },
				{ "REVENDA", "10", "5", "5566", NCM3 } };

		for (String[] dados : data) {
			int aux = 0;
			DocumentoFiscalItem docFiscalItem = new DocumentoFiscalItem();
			docFiscalItem.setFinalidade(Finalidade.valueOf(dados[aux++]));
			docFiscalItem.setQuantidade(new BigDecimal(dados[aux++]));
			docFiscalItem.setValorUnitario(new BigDecimal(dados[aux++]));
			docFiscalItem.setCfop(Integer.parseInt(dados[aux++]));
			int ncmCodigo = Integer.parseInt(dados[aux++]);
			docFiscalItem.setNcm(ncms.stream().filter(ncm -> ncm.getNumero() == ncmCodigo).findAny().get());
			documentoFiscalItens.add(docFiscalItem);
		}

		return documentoFiscalItens;
	}

	public void cleanUp() {
		estadoRepository.deleteAll();
		operacaoRepository.deleteAll();
		ncmRepository.deleteAll();
		tributacaoEstadualRepository.deleteAll();
		tributacaoFederalRepository.deleteAll();
		docFiscalItemRepository.deleteAll();
		docFiscalRepository.deleteAll();
	}
}
