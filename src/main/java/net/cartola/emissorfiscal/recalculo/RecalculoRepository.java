package net.cartola.emissorfiscal.recalculo;

import net.cartola.emissorfiscal.ncm.Ncm;
import net.cartola.emissorfiscal.operacao.Operacao;
import net.cartola.emissorfiscal.tributacao.estadual.TributacaoEstadual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecalculoRepository extends JpaRepository<TributacaoEstadual, Long> {

    @Query("SELECT t FROM TributacaoEstadual t LEFT JOIN t.ncm n WHERE t.operacao.id = :operacaoId AND n.numero IN :ncmList")
    List<TributacaoEstadual> findByNcmAndOperacao(List ncmList, Long operacaoId);

}

