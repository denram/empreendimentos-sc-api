package br.com.denram.empreendimentos_sc_api.repository;

import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    Page<Municipio> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}