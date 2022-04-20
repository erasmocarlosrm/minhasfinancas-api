package com.financas.minmhasfinancas.servicecos.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.minmhasfinancas.exception.ErroAutenticacao;
import com.financas.minmhasfinancas.exception.RegraNegocioExiception;
import com.financas.minmhasfinancas.model.entidade.Usuario;
import com.financas.minmhasfinancas.repositorio.UsuarioRepositorio;
import com.financas.minmhasfinancas.servicos.UsuarioService;

@Service
public class UsuarioServeImp implements UsuarioService {


	@Autowired
	private UsuarioRepositorio usarioRepositorio ;
	
	 
	public UsuarioServeImp(UsuarioRepositorio usarioRepositorio) {
		super();
		this.usarioRepositorio = usarioRepositorio;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		
	  Optional<Usuario> usuario = usarioRepositorio.findByEmail(email);
	  
	  if(!usuario.isPresent()) {
		  throw new ErroAutenticacao("Usuário não entrodado para o E-mail informado");
	  }
	  
	  if(!usuario.get().getSenha().equals(senha)) {
		  throw new ErroAutenticacao("Senha inválida.");
		  
	  }
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		
		validarEmail(usuario.getEmail());	
	
		return usarioRepositorio.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe =  usarioRepositorio.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioExiception("Já existe um usuario cadastrado com esse E-mail ");
		}
		
		
	}

}
