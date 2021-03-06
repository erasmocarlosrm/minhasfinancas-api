package com.financas.minmhasfinancas.servicecos.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.minmhasfinancas.enums.StatuLancamento;
import com.financas.minmhasfinancas.enums.TipoLancamento;
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
			
			throw new RegraNegocioExiception("Informe a Descri????o v??lido");
		}
		
		if(validarLacamento.getAno() == null || validarLacamento.getAno().toString().length() != 4) {
			throw new RegraNegocioExiception("Informe um Ano v??lido");
		}
		
		if (validarLacamento.getMes() == null || validarLacamento.getMes() < 1 || validarLacamento.getMes() > 12) {
			throw new RegraNegocioExiception("Informe um M??s v??lido");
		}
		
		if (validarLacamento.getUsuario() == null || validarLacamento.getId() == null) {
			throw new RegraNegocioExiception("Informe um Usu??rio");
		}
		
		if (validarLacamento.getValor() == null || validarLacamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioExiception("Informe um Valor v??lido");
		}
		
		if (validarLacamento.getTipoLancamento() == null) {
			throw new RegraNegocioExiception("Informe um tipo v??lido");
		}
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return repositorioLancamento.findById(id) ;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoUsuario(Long id) {
		BigDecimal receitas =   repositorioLancamento.obtgerSaldoPorTipoLancamento(id, TipoLancamento.RECEITA.name());
		BigDecimal despesas = 	repositorioLancamento.obtgerSaldoPorTipoLancamento(id, TipoLancamento.DESPESA.name());
		
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if (despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		return receitas.subtract(despesas);
	}

}
