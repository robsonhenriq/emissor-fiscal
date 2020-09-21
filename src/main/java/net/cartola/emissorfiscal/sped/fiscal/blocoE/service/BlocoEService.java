package net.cartola.emissorfiscal.sped.fiscal.blocoE.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.sped.fiscal.MontaBloco;
import net.cartola.emissorfiscal.sped.fiscal.MovimentacoesMensalIcmsIpi;
import net.cartola.emissorfiscal.sped.fiscal.bloco0.service.Bloco0Service;
import net.cartola.emissorfiscal.sped.fiscal.blocoE.BlocoE;

/**
 * 21/09/2020
 * @author robson.costa
 */
@Service
public class BlocoEService implements MontaBloco<BlocoE, MovimentacoesMensalIcmsIpi> {

	private static final Logger LOG = Logger.getLogger(BlocoEService.class.getName());

	@Override
	public BlocoE criarBloco(MovimentacoesMensalIcmsIpi movimentacoesMensalIcmsIpi) {
		// TODO Auto-generated method stub
		LOG.log(Level.INFO, "Montando o bloco E, com INICIO em: {0} e TERMINO: {1} ", movimentacoesMensalIcmsIpi.getDataInicio());
		return null;
	}

	
}
