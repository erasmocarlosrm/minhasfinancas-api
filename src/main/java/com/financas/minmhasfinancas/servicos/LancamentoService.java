package com.financas.minmhasfinancas.servicos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.financas.minmhasfinancas.enums.StatuLancamento;
import com.financas.minmhasfinancas.model.entidade.Lancamento;

public interface LancamentoService {
	
	
	public Lancamento salvarLancamento(Lancamento lancamento );
	
	public Lancamento atualizarLacamento (Lancamento lacamentoAtualizar);
	
	public void deletarLancamento (Lancamento lacamentoDeletar);
	
	public List<Lancamento> buscarLancamento(Lancamento lacamentoBuscar);
	
	public void atualizarStatusLancamento (Lancamento lacamentoStatus, StatuLancamento status);
	
	public void validarLancamento(Lancamento validarLacamento);
	
	public Optional< Lancamento> buscarPorId(Long id);
	
	BigDecimal obterSaldoUsuario(Long id);

}
