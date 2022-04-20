package com.financas.minmhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException {
	
	

	private static final long serialVersionUID = 1L;

	public  ErroAutenticacao (String mensagemErro) {
		
		super(mensagemErro);
		
	}
	

}
