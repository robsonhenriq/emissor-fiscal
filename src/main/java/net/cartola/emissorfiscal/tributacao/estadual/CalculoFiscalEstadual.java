package net.cartola.emissorfiscal.tributacao.estadual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.documento.DocumentoFiscal;
import net.cartola.emissorfiscal.documento.DocumentoFiscalItem;
import net.cartola.emissorfiscal.ncm.Ncm;
import net.cartola.emissorfiscal.tributacao.CalculoFiscal;

@Service
public class CalculoFiscalEstadual implements CalculoFiscal {
	
	@Autowired
	TributacaoEstadualRepository tributacaoEstadualRepository;
	
	@Autowired
	CalculoIcms calculoIcms;

	@Override
	public Map<String, String> calculaImposto(DocumentoFiscal documentoFiscal) {
		Map<String, String> resultMap = new HashMap<>();
		Set<Ncm> ncms = documentoFiscal.getItens().stream().map(DocumentoFiscalItem::getNcm).collect(Collectors.toSet());
		
		Map<Ncm, TributacaoEstadual> mapaTributacoes = new HashMap<>();
		for(Ncm ncm:ncms) {
			List<TributacaoEstadual> tributacaoEstaduals = tributacaoEstadualRepository.findByNcm(ncm);
			mapaTributacoes.put(ncm, tributacaoEstaduals.get(0));
		}
		
		documentoFiscal.getItens().forEach(di -> {
			TributacaoEstadual tributacao = mapaTributacoes.get(di.getNcm());
			Map<String, String> rm = calculoIcms.calcula(di, tributacao);
		});
		
		totaliza(documentoFiscal);

		return resultMap;
	}

	/**
	 * Calcula a soma do ICMS para os itens
	 * @param documentoFiscal
	 */
	private void totaliza(DocumentoFiscal documentoFiscal) {
		// TODO Auto-generated method stub
		
	}
	
	

}
