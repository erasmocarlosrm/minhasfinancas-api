package com.financas.minmhasfinancas.repositorio;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financas.minmhasfinancas.model.entidade.Lancamento;

public interface LancamentoRepositorio extends JpaRepository<Lancamento, Long> {
	
	
		@Query(value = "select sum(l.valor) from Lancamento l join l.usuario u"
					 + " where u.id = :idUsuario and l.tipoLancamento=:tipoLancamento group by u")
		BigDecimal obtgerSaldoPorTipoLancamento(@Param("idUsuario") Long idUsuario, @Param("tipoLancamento") String tipoLancamento);

}
