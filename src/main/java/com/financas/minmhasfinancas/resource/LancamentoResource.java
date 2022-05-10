package com.financas.minmhasfinancas.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.minmhasfinancas.api.dto.AtualizaStatusDTO;
import com.financas.minmhasfinancas.api.dto.LacamentoDTO;
import com.financas.minmhasfinancas.enums.StatuLancamento;
import com.financas.minmhasfinancas.enums.TipoLancamento;
import com.financas.minmhasfinancas.exception.RegraNegocioExiception;
import com.financas.minmhasfinancas.model.entidade.Lancamento;
import com.financas.minmhasfinancas.model.entidade.Usuario;
import com.financas.minmhasfinancas.servicos.LancamentoService;
import com.financas.minmhasfinancas.servicos.UsuarioService;

@RestController
@RequestMapping("/api/lacamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping
	public ResponseEntity buscarLancamento(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes, 
			@RequestParam(value = "ano", required = false) Integer ano, 
			@RequestParam("usuario") Long idUsuario
			) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario =  usuarioService.buscarUsuario(idUsuario);
		if (usuario.isPresent()) {
			
			return ResponseEntity.badRequest().body("Usuário não encontrado para o ID informado");
			
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscarLancamento(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}

	@PostMapping
	public ResponseEntity salvarLancamento(@RequestBody LacamentoDTO lacanmentoDTO) {

		try {

			Lancamento entidadesalva = convertLacamento(lacanmentoDTO);
			entidadesalva = lancamentoService.salvarLancamento(entidadesalva);

			return new ResponseEntity(entidadesalva, HttpStatus.CREATED);

		} catch (RegraNegocioExiception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PutMapping("{id}")
	public ResponseEntity atualizarLancamento(@PathVariable("id") Long id, @RequestBody LacamentoDTO lacamentoDTO) {
		return lancamentoService.buscarPorId(id).map(entity -> {

			try {
				Lancamento lancamento = convertLacamento(lacamentoDTO);
				lancamento.setId(entity.getId());
				lancamentoService.atualizarLacamento(lancamento);
				return ResponseEntity.ok(lancamento);

			} catch (RegraNegocioExiception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));

	}
	
	@PutMapping("{id}/atualizar-sattus")
	public ResponseEntity atualizarStatusLancamento( @PathVariable("id") Long id, @RequestBody AtualizaStatusDTO statusDTO) {
		
		
		return lancamentoService.buscarPorId(id).map(entity -> {
		StatuLancamento statusSelecionado = 	StatuLancamento.valueOf(statusDTO.getStatus());
		
		if (statusSelecionado == null) {
			
			return ResponseEntity.badRequest().body("Não foi possível atualiar status do lançameto, status invalido");
			
		}
		try {
			entity.setStatusLacamento(statusSelecionado);
			 lancamentoService.atualizarLacamento(entity);
			 return ResponseEntity.ok(entity);	
		} catch (RegraNegocioExiception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		 
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
		
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletarLancamento(@PathVariable("id") Long id) {

		return lancamentoService.buscarPorId(id).map(entidade -> {
			try {
				lancamentoService.deletarLancamento(entidade);
				return new ResponseEntity(HttpStatus.NO_CONTENT);

			} catch (RegraNegocioExiception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));

	}

	private Lancamento convertLacamento(LacamentoDTO dtoLacamento) {
		Lancamento lancamento = new Lancamento();

		lancamento.setDescricao(dtoLacamento.getDecricao());
		lancamento.setId(dtoLacamento.getId());
		lancamento.setAno(dtoLacamento.getAno());
		lancamento.setMes(dtoLacamento.getMes());
		lancamento.setValor(dtoLacamento.getValor());

		Usuario usuario = usuarioService.buscarUsuario(dtoLacamento.getUsuario())
				.orElseThrow(() -> new RegraNegocioExiception("Usuário não encontrado"));

		lancamento.setUsuario(usuario);
		lancamento.setTipoLancamento(TipoLancamento.valueOf(dtoLacamento.getTipo()));
		lancamento.setStatusLacamento(StatuLancamento.valueOf(dtoLacamento.getStatus()));

		return lancamento;
	}

}
