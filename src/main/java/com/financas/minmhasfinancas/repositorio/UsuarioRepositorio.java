package com.financas.minmhasfinancas.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.minmhasfinancas.model.entidade.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	
	
	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);
	


}
