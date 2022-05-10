package com.financas.minmhasfinancas.servicos;

import java.util.Optional;

import com.financas.minmhasfinancas.model.entidade.Usuario;

public interface UsuarioService  {
	
		public Usuario autenticar(String email, String senha);		
		
		public Usuario salvarUsuario (Usuario usuario);
		
		public void validarEmail(String email);
		
		public Optional< Usuario> buscarUsuario(Long id);
		
	

}
