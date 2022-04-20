package com.financas.minmhasfinancas.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.minmhasfinancas.api.dto.UsuarioDTO;
import com.financas.minmhasfinancas.exception.ErroAutenticacao;
import com.financas.minmhasfinancas.exception.RegraNegocioExiception;
import com.financas.minmhasfinancas.model.entidade.Usuario;
import com.financas.minmhasfinancas.servicos.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService userarioService;

	@PostMapping("/autenticar")
	public ResponseEntity autenticarUsuario(@RequestBody UsuarioDTO dtoAutenticaUsuario) {

		try {

			Usuario usuarioAutenciado = userarioService.autenticar(dtoAutenticaUsuario.getEmail(),
					dtoAutenticaUsuario.getSenha());

			return ResponseEntity.ok(usuarioAutenciado);

		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping
	public ResponseEntity salvarUsuaruio(@RequestBody UsuarioDTO dtoUsuario) {

		Usuario usuario = Usuario.builder().nome(dtoUsuario.getNome()).email(dtoUsuario.getEmail())
				.senha(dtoUsuario.getSenha()).build();

		try {

			Usuario usuarioSalvo = userarioService.salvarUsuario(usuario);

			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);

		} catch (RegraNegocioExiception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
