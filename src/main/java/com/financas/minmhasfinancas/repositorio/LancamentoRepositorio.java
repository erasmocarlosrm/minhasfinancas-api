package com.financas.minmhasfinancas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.minmhasfinancas.model.entidade.Lancamento;

public interface LancamentoRepositorio extends JpaRepository<Lancamento, Long> {

}
