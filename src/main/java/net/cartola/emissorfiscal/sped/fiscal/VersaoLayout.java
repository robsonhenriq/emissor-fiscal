package net.cartola.emissorfiscal.sped.fiscal;

/**
 * 19/08/2020
 * @author robson.costa
 */
public enum VersaoLayout {

	/**
	 *  Leiaute válido de 01/01/2020 a 31/12/2020 - publicado pelo Ato Cotepe nº 65/2019.
	 */
	V_014("014");
	
	
	private String descricao;
	 
	VersaoLayout(String descricao) {
        this.descricao = descricao;
    }
 
    public String getDescricao() {
        return descricao;
    }
    
    
}
