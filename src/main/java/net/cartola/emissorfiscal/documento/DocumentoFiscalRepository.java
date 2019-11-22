package net.cartola.emissorfiscal.documento;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.cartola.emissorfiscal.operacao.Operacao;

@Repository
public interface DocumentoFiscalRepository extends JpaRepository<DocumentoFiscal, Long> {

	List<DocumentoFiscal> findByOperacao(Operacao operacao);

	List<DocumentoFiscal> findByOperacaoIn(Collection<Operacao> operacoes);

//	@Query("SELECT * FROM DocumentoFiscal WHERE DocumentoFiscal ")
//	DocumentoFiscal findDocumentoFiscalByCnpjTipoDocumentoSerieNumero(String cnpjEmitente, String tipoDocumento, String serie,
//			String numero);

}
