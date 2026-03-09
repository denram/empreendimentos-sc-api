package br.com.denram.empreendimentos_sc_api.specification;

import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import org.springframework.data.jpa.domain.Specification;

public final class EmpreendimentoSpecification {

    private EmpreendimentoSpecification() {
    }

    public static Specification<Empreendimento> nomeContendo(String nome) {
        return (root, query, criteriaBuilder) ->
                nome == null || nome.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.trim().toLowerCase() + "%");
    }

    public static Specification<Empreendimento> municipioNomeContendo(String municipioNome) {
        return (root, query, criteriaBuilder) ->
                municipioNome == null || municipioNome.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("municipio").get("nome")),
                        "%" + municipioNome.trim().toLowerCase() + "%");
    }

    public static Specification<Empreendimento> segmentoIgual(Segmento segmento) {
        return (root, query, criteriaBuilder) ->
                segmento == null
                        ? null
                        : criteriaBuilder.equal(root.get("segmento"), segmento);
    }
}