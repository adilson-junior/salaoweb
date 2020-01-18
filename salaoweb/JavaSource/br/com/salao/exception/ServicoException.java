package br.com.salao.exception;

public class ServicoException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public ServicoException() {		
	}

	public ServicoException(String message) {
		super(message);		
	}

	public ServicoException(Throwable cause) {
		super(cause);		
	}

	public ServicoException(String message, Throwable cause) {
		super(message, cause);
	}

}
