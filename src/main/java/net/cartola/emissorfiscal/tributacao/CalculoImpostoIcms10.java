package net.cartola.emissorfiscal.tributacao;

import java.math.BigDecimal;

public class CalculoImpostoIcms10 extends CalculoImpostoFcp {

	private static final long serialVersionUID = -6800234562794581141L;
	
	private String modalidadeDaBaseCalculoSt;
	private BigDecimal baseDeCalculoSt;
	private BigDecimal iva;
	private BigDecimal aliquotaIcmsSt;
	private BigDecimal valorIcmsSt;
	
	private BigDecimal aliqReducaoBaseSt;
//	protected BigDecimal reducaoBaseAliquota;			//	setIcmsReducaoBaseAliquota
//	protected BigDecimal vlrBaseFcp;
//	protected BigDecimal fcpAliquota = BigDecimal.ZERO;
//	protected BigDecimal valorFcp;
	
	private BigDecimal vlrBaseFcpSt;
	private BigDecimal aliquotaFcpSt;
	private BigDecimal valorFcpSt;

	public String getModalidadeDaBaseCalculoSt() {
		return modalidadeDaBaseCalculoSt;
	}

	public void setModalidadeDaBaseCalculoSt(String modalidadeDaBaseCalculoSt) {
		this.modalidadeDaBaseCalculoSt = modalidadeDaBaseCalculoSt;
	}

	public BigDecimal getBaseDeCalculoSt() {
		return baseDeCalculoSt;
	}

	public void setBaseDeCalculoSt(BigDecimal baseDeCalculoSt) {
		this.baseDeCalculoSt = baseDeCalculoSt;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getAliquotaIcmsSt() {
		return aliquotaIcmsSt;
	}

	public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
		this.aliquotaIcmsSt = aliquotaIcmsSt;
	}

	public BigDecimal getValorIcmsSt() {
		return valorIcmsSt;
	}

	public void setValorIcmsSt(BigDecimal valorIcmsSt) {
		this.valorIcmsSt = valorIcmsSt;
	}
	
	public BigDecimal getAliqReducaoBaseSt() {
		return aliqReducaoBaseSt;
	}

	public void setAliqReducaoBaseSt(BigDecimal aliqReducaoBaseSt) {
		this.aliqReducaoBaseSt = aliqReducaoBaseSt;
	}

	public BigDecimal getVlrBaseFcpSt() {
		return vlrBaseFcpSt;
	}

	public void setVlrBaseFcpSt(BigDecimal vlrBaseFcpSt) {
		this.vlrBaseFcpSt = vlrBaseFcpSt;
	}

	public BigDecimal getAliquotaFcpSt() {
		return aliquotaFcpSt;
	}

	public void setAliquotaFcpSt(BigDecimal aliquotaFcpSt) {
		this.aliquotaFcpSt = aliquotaFcpSt;
	}

	public BigDecimal getValorFcpSt() {
		return valorFcpSt;
	}

	public void setValorFcpSt(BigDecimal valorFcpSt) {
		this.valorFcpSt = valorFcpSt;
	}
	
}
