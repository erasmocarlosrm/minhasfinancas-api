package com.financas.minmhasfinancas.servicos;

import com.financas.minmhasfinancas.model.entidade.Usuario;

public interface UsuarioService  {
	
		public Usuario autenticar(String email, String senha);		
		
		public Usuario salvarUsuario (Usuario usuario);
		
		public void validarEmail(String email);

}
