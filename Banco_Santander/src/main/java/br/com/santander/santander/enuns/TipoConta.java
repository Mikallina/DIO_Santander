package br.com.santander.santander.enuns;


public enum TipoConta {

	CORRENTE(1), POUPANCA(2);

	private final Integer codigo;

	private TipoConta(Integer codigo) {
		this.codigo = codigo;

	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getTipoAbreviado() {
		return this == CORRENTE ? "CC" : "CP";
	}

	/*
	 * public static TipoConta fromCodigo(int codigo) { for (TipoConta tipo :
	 * TipoConta.values()) { if (tipo.getCodigo() == codigo) { return tipo; } }
	 * throw new IllegalArgumentException("Código inválido para TipoConta: " +
	 * codigo); }
	 */

}
