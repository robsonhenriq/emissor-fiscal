package net.cartola.emissorfiscal.sped.fiscal.blocoG;

import java.util.List;

import coffeepot.bean.wr.annotation.Field;
import coffeepot.bean.wr.annotation.Record;

/**
 * 14/09/2020
 * @author robson.costa
 */
//@Record(fields = { 
//	@Field(name = "reg", maxLength = 4),
//
//})
public class RegG140 {
	
	private final String reg = "G140";

	/** 
	 * COLOCAR os ATRIBUTOS QUE CONSTAM na DOCUMENTAÇÃO
	 */
	
	 // ==================== FILHOS ====================
	private List<RegG126> regG126;
	private List<RegG130> regG130;
}
