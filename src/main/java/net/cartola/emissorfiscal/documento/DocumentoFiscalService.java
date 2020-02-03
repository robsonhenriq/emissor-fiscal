package net.cartola.emissorfiscal.documento;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.ncm.Ncm;
import net.cartola.emissorfiscal.ncm.NcmService;
import net.cartola.emissorfiscal.operacao.Operacao;
import net.cartola.emissorfiscal.operacao.OperacaoService;
import net.cartola.emissorfiscal.pessoa.Pessoa;
import net.cartola.emissorfiscal.pessoa.PessoaService;
import net.cartola.emissorfiscal.tributacao.estadual.CalculoFiscalEstadual;
import net.cartola.emissorfiscal.tributacao.estadual.TributacaoEstadual;
import net.cartola.emissorfiscal.tributacao.estadual.TributacaoEstadualService;
import net.cartola.emissorfiscal.tributacao.federal.CalculoFiscalFederal;
import net.cartola.emissorfiscal.tributacao.federal.TributacaoFederalService;
import net.cartola.emissorfiscal.util.ValidationHelper;

@Service
public class DocumentoFiscalService {

	@Autowired
	private DocumentoFiscalRepository documentoFiscalRepository;
	
	@Autowired
	private OperacaoService operacaoService;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired 
	private NcmService ncmService;
	
	@Autowired
	private TributacaoEstadualService icmsService;
	
	@Autowired
	private CalculoFiscalEstadual calcFiscalEstadual;
	
	@Autowired
	private CalculoFiscalFederal calcFiscalFederal;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public DocumentoFiscalDto convertToDto(DocumentoFiscal docFiscal) {
		DocumentoFiscalDto docFiscalDto = modelMapper.map(docFiscal, DocumentoFiscalDto.class);
		return docFiscalDto;
	}
	
	public DocumentoFiscal convertToEntity(DocumentoFiscalDto docFiscalDto) throws ParseException {
		DocumentoFiscal docFiscal = modelMapper.map(docFiscalDto, DocumentoFiscal.class);
		if (docFiscalDto.getId() != null) {
			DocumentoFiscal oldDocFiscal = findOne(docFiscalDto.getId()).get();
			docFiscal.setId(oldDocFiscal.getId());
//			docFiscal.
		}
		return docFiscal;
	}
	
	public List<DocumentoFiscal> findAll() {
		return documentoFiscalRepository.findAll();
	}
	
	public Optional<DocumentoFiscal> save(DocumentoFiscal documentoFiscal) {
		calcFiscalEstadual.calculaImposto(documentoFiscal);
		calcFiscalFederal.calculaImposto(documentoFiscal);
		return Optional.ofNullable(documentoFiscalRepository.saveAndFlush(documentoFiscal));
	}
	
	public List<DocumentoFiscal> findDocumentoFiscalByOperacao(Operacao operacao) {
		return documentoFiscalRepository.findByOperacao(operacao);
	}
	
	public List<DocumentoFiscal> findDocumentoFiscalByVariasOperacoes(Collection<Operacao> operacoes) {
		return documentoFiscalRepository.findByOperacaoIn(operacoes);
	}
	
	public Optional<DocumentoFiscal> findOne(Long id) {
		return documentoFiscalRepository.findById(id);
	}

	public void deleteById(Long id) {
		documentoFiscalRepository.deleteById(id);
	}
	
	public Optional<DocumentoFiscal> findDocumentoFiscalByCnpjTipoDocumentoSerieENumero(Long cnpjEmitente, String tipoDocumento, Long serie, Long numero) {
		return documentoFiscalRepository.findDocumentoFiscalByEmitenteCnpjAndTipoAndSerieAndNumero(cnpjEmitente,  tipoDocumento,  serie,  numero);
	}
	
	/**
	 * Valida se as informações necessárias para um documento Fiscal existem, caso sim, as mesmas são setadas no 
	 * documentoFiscal
	 * @param documentoFiscal
	 * @return
	 */
	public List<String> validaDadosESetaValoresNecessarios(DocumentoFiscal documentoFiscal) {
		Map<String, Boolean> map = new HashMap<>();
		Optional<Operacao> opOperacao = operacaoService.findOperacaoByDescricao(documentoFiscal.getOperacao().getDescricao());
		List<Pessoa> opEmitente = pessoaService.findByCnpj(documentoFiscal.getEmitente().getCnpj());
		List<Pessoa> opDestinatario = pessoaService.findByCnpj(documentoFiscal.getDestinatario().getCnpj());
	
		documentoFiscal.getItens().forEach(docItem -> {
			Optional<Ncm> opNcm = ncmService.findNcmByNumeroAndExcecao(docItem.getNcm().getNumero(), docItem.getNcm().getExcecao());
			if(opNcm.isPresent()) {
				docItem.setNcm(opNcm.get());
			}
			map.put("O NCM: " +docItem.getNcm().getNumero()+ " NÃO existe", opNcm.isPresent());
		});
		
		Set<Ncm> ncms = documentoFiscal.getItens().stream().map(DocumentoFiscalItem::getNcm).collect(Collectors.toSet());
		List<TributacaoEstadual> listTributacoes = icmsService.findTributacaoEstadualByOperacaoENcms(opOperacao.get(), ncms);
		
		map.put("A operação: " +documentoFiscal.getOperacao().getDescricao()+ " NÃO existe", opOperacao.isPresent());
		map.put("O CNPJ: " +documentoFiscal.getEmitente().getCnpj()+ " do emitente NÃO existe" , !opEmitente.isEmpty());
		map.put("O CNPJ: " +documentoFiscal.getDestinatario().getCnpj()+ " do destinatário NÃO existe", !opDestinatario.isEmpty());
		map.put("Não existe tributação para essa OPERAÇÃO e os NCMS dos itens", !listTributacoes.isEmpty());
		
		if (opOperacao.isPresent() && !opEmitente.isEmpty() && !opDestinatario.isEmpty()) {
			documentoFiscal.setOperacao(opOperacao.get());
			documentoFiscal.setEmitente(opEmitente.get(0));
			documentoFiscal.setDestinatario(opDestinatario.get(0));
		}
		return ValidationHelper.processaErros(map);
	}
	
}
