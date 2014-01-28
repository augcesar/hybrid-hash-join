package br.unifor.negocio;

import br.unifor.gui.Principal;

public class Logger {
	private Principal principal;
	
	public Logger(Principal p) {
		this.principal = p;
	}
	
	public void setLogPrincipal(String mensagem) {
		principal.setLog(mensagem);
	}

}
