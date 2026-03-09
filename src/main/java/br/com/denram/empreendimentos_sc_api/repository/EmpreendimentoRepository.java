package br.com.denram.empreendimentos_sc_api.repository;

import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmpreendimentoRepository extends JpaRepository<Empreendimento, Long>, JpaSpecificationExecutor<Empreendimento> {
}
