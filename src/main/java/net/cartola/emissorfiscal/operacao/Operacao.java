package net.cartola.emissorfiscal.operacao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 08/08/2017 17:44:37
 *
 * @author murilo
 */
@Entity
@Table(name = "oper", uniqueConstraints = { @UniqueConstraint(name = "unk_oper_dscr", columnNames = {"dscr"}) })
public class Operacao implements Serializable, Comparable<Operacao> {

	private static final long serialVersionUID = 12701892348311L;
	private Long id;
	private String descricao;
	private boolean isDevolucao;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oper_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "dscr", nullable = false)
	@NotNull(message="A DESCRIÇÃO não pode ser NULA")
	@NotBlank(message="A DESCRIÇÃO tem que ser preenchida")
	@Length(message= "A DESCRIÇÃO deve ter entre 3 á 150 caracteres", min=3, max=150)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "devolucao", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0' ")
	public boolean isDevolucao() {
		return isDevolucao;
	}

	public void setDevolucao(boolean isDevolucao) {
		this.isDevolucao = isDevolucao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Operacao)) {
			return false;
		}
		Operacao other = (Operacao) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "Operacao[id=" + id + ", descricao=" + descricao + "]";
	}

	@Override
	public int compareTo(Operacao obj) {
		Long i = (this.id - obj.id);
		return i.intValue();
	}

}
