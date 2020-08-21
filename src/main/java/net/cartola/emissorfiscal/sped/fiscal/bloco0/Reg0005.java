package net.cartola.emissorfiscal.sped.fiscal.bloco0;

import coffeepot.bean.wr.annotation.Field;
import coffeepot.bean.wr.annotation.Record;

/**
 * 19/08/2020
 * @author robson.costa
 * 
 * Registro 0005 - Dados Complementares da entidade
 */
@Record(fields = {
		@Field(name = "reg", maxLength = 4),	    
		@Field(name = "fantansia"),
		@Field(name = "cep"),	    
		@Field(name = "end"),
		@Field(name = "num"),	    
		@Field(name = "compl"),
		@Field(name = "bairro"),	    
		@Field(name = "fone"),
		@Field(name = "fax"),	    
		@Field(name = "email")
})
public class Reg0005 {

	private final String reg = "0005";
	private String fantansia;
	private Long cep;
	private String end;
	private int num;
	private String compl;
	private String bairro;
	private String fone;
	private String fax;
	private String email;
	
	public String getReg() {
		return reg;
	}
	
	public String getFantansia() {
		return fantansia;
	}

	public void setFantansia(String fantansia) {
		this.fantansia = fantansia;
	}

	public Long getCep() {
		return cep;
	}

	public void setCep(Long cep) {
		this.cep = cep;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCompl() {
		return compl;
	}

	public void setCompl(String compl) {
		this.compl = compl;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
