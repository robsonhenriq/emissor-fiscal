
package net.cartola.emissorfiscal.sped.fiscal.bloco0.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import net.cartola.emissorfiscal.sped.fiscal.MontaGrupoDeRegistroList;
import net.cartola.emissorfiscal.sped.fiscal.MovimentacoesMensalIcmsIpi;
import net.cartola.emissorfiscal.sped.fiscal.bloco0.Reg0400;

/**
 * 21/09/2020
 * @author robson.costa
 */
@Service
class Reg0400Service implements MontaGrupoDeRegistroList<Reg0400, MovimentacoesMensalIcmsIpi> {

	private static final Logger LOG = Logger.getLogger(Reg0400Service.class.getName());
	
	@Override
	public List<Reg0400> montarGrupoDeRegistro(MovimentacoesMensalIcmsIpi movimentosIcmsIpi) {
		// TODO Auto-generated method stub
		LOG.log(Level.INFO, "Montando o Grupo de Registro 0400 ");

		return null;
	}

}
