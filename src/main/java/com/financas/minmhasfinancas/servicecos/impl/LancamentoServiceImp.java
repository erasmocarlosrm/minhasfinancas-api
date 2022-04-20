package com.financas.minmhasfinancas.servicecos.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.minmhasfinancas.enums.StatuLancamento;
import com.financas.minmhasfinancas.exception.RegraNegocioExiception;
import com.financas.minmhasfinancas.model.entidade.Lancamento;
import com.financas.minmhasfinancas.repositorio.LancamentoRepositorio;
import com.financas.minmhasfinancas.servicos.LancamentoService;


@Service
public class LancamentoServiceImp implements LancamentoService {

	
	@Autowired
	private LancamentoRepositorio repositorioLancamento;
	
	
	@Override
	@Transactional
	public Lancamento salvarLancamento(Lancamento lancamento) {
		validarLancamento(lancamento);		
		lancamento.setStatusLacamento(StatuLancamento.PENDENTE);
		return repositorioLancamento.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizarLacamento(Lancamento lacamentoAtualizar) {
				
		Objects.requireNonNull(lacamentoAtualizar.getId());
		validarLancamento(lacamentoAtualizar);
		 return repositorioLancamento.save(lacamentoAtualizar);
	}

	@Override
	@Transactional
	public void deletarLancamento(Lancamento lacamentoDeletar) {
		
		Objects.requireNonNull(lacamentoDeletar.getId());
		
		repositorioLancamento.delete(lacamentoDeletar);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscarLancamento(Lancamento lacamentoFiltro) {
		
		Example example = Example.of(lacamentoFiltro, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repositorioLancamento.findAll(example);
	}

	@Override
	public void atualizarStatusLancamento(Lancamento lacamentoStatus, StatuLancamento status) {
		
		lacamentoStatus.setStatusLacamento(status);
		
		atualizarLacamento(lacamentoStatus);
	}

	@Override
	public void validarLancamento(Lancamento validarLacamento) {
	
		if (validarLacamento.getDescricao() == null || validarLacamento.getDescricao().trim().equals("")) {
			
			throw new RegraNegocioExiception("Informe a Descrição válido");
		}
		
		if(validarLacamento.getAno() == null || validarLacamento.getAno().toString().length() != 4) {
			throw new RegraNegocioExiception("Informe um Ano válido");
		}
		
		if (validarLacamento.getMes() == null || validarLacamento.getMes() < 1 || validarLacamento.getMes() > 12) {
			throw new RegraNegocioExiception("Informe um Mês válido");
		}
		
		if (validarLacamento.getUsuario() == null || validarLacamento.getId() == null) {
			throw new RegraNegocioExiception("Informe um Usuário");
		}
		
		if (validarLacamento.getValor() == null || validarLacamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioExiception("Informe um Valor válido");
		}
		
		if (validarLacamento.getTipoLancamento() == null) {
			throw new RegraNegocioExiception("Informe um tipo válido");
		}
	}

}
