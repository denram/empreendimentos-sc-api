package br.com.denram.empreendimentos_sc_api.specification;

import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpreendimentoSpecification")
class EmpreendimentoSpecificationTest {

    @Mock
    private Root<Empreendimento> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Mock
    private Path<String> nomePath;

    @Mock
    private Path<Object> municipioPath;

    @Mock
    private Path<String> municipioNomePath;

    @Mock
    private Path<Segmento> segmentoPath;

    @Nested
    @DisplayName("nomeContendo")
    class NomeContendo {

        @ParameterizedTest(name = "deve retornar null quando nome for \"{0}\"")
        @MethodSource("br.com.denram.empreendimentos_sc_api.specification.EmpreendimentoSpecificationTest#stringsNulasOuEmBranco")
        void deveRetornarNullQuandoNomeForVazio(String nome) {
            Specification<Empreendimento> specification = EmpreendimentoSpecification.nomeContendo(nome);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertNull(resultado);
            verifyNoInteractions(root, criteriaBuilder);
        }

        @Test
        @DisplayName("deve criar predicate like com nome normalizado")
        void deveCriarPredicateLikeComNomeNormalizado() {
            String nome = "  MerCado Azul  ";

            when(root.<String>get("nome")).thenReturn(nomePath);
            when(criteriaBuilder.lower(nomePath)).thenReturn(nomePath);
            when(criteriaBuilder.like(nomePath, "%mercado azul%")).thenReturn(predicate);

            Specification<Empreendimento> specification = EmpreendimentoSpecification.nomeContendo(nome);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertSame(predicate, resultado);
            verify(root).<String>get("nome");
            verify(criteriaBuilder).lower(nomePath);
            verify(criteriaBuilder).like(nomePath, "%mercado azul%");
            verifyNoMoreInteractions(criteriaBuilder);
        }
    }

    @Nested
    @DisplayName("municipioNomeContendo")
    class MunicipioNomeContendo {

        @ParameterizedTest(name = "deve retornar null quando municipioNome for \"{0}\"")
        @MethodSource("br.com.denram.empreendimentos_sc_api.specification.EmpreendimentoSpecificationTest#stringsNulasOuEmBranco")
        void deveRetornarNullQuandoMunicipioNomeForVazio(String municipioNome) {
            Specification<Empreendimento> specification =
                    EmpreendimentoSpecification.municipioNomeContendo(municipioNome);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertNull(resultado);
            verifyNoInteractions(root, criteriaBuilder);
        }

        @Test
        @DisplayName("deve criar predicate like com nome do município normalizado")
        void deveCriarPredicateLikeComMunicipioNomeNormalizado() {
            String municipioNome = "  JoinVille  ";

            when(root.<Object>get("municipio")).thenReturn(municipioPath);
            when(municipioPath.<String>get("nome")).thenReturn(municipioNomePath);
            when(criteriaBuilder.lower(municipioNomePath)).thenReturn(municipioNomePath);
            when(criteriaBuilder.like(municipioNomePath, "%joinville%")).thenReturn(predicate);

            Specification<Empreendimento> specification =
                    EmpreendimentoSpecification.municipioNomeContendo(municipioNome);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertSame(predicate, resultado);
            verify(root).<Object>get("municipio");
            verify(municipioPath).<String>get("nome");
            verify(criteriaBuilder).lower(municipioNomePath);
            verify(criteriaBuilder).like(municipioNomePath, "%joinville%");
            verifyNoMoreInteractions(criteriaBuilder);
        }
    }

    @Nested
    @DisplayName("segmentoIgual")
    class SegmentoIgual {

        @Test
        @DisplayName("deve retornar null quando segmento for null")
        void deveRetornarNullQuandoSegmentoForNull() {
            Specification<Empreendimento> specification = EmpreendimentoSpecification.segmentoIgual(null);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertNull(resultado);
            verifyNoInteractions(root, criteriaBuilder);
        }

        @Test
        @DisplayName("deve criar predicate equal quando segmento for informado")
        void deveCriarPredicateEqualQuandoSegmentoForInformado() {
            Segmento segmento = primeiroSegmentoDisponivel();

            when(root.<Segmento>get("segmento")).thenReturn(segmentoPath);
            when(criteriaBuilder.equal(segmentoPath, segmento)).thenReturn(predicate);

            Specification<Empreendimento> specification = EmpreendimentoSpecification.segmentoIgual(segmento);

            Predicate resultado = specification.toPredicate(root, query, criteriaBuilder);

            assertSame(predicate, resultado);
            verify(root).<Segmento>get("segmento");
            verify(criteriaBuilder).equal(segmentoPath, segmento);
            verifyNoMoreInteractions(criteriaBuilder);
        }
    }

    static Stream<String> stringsNulasOuEmBranco() {
        return Stream.of(null, "", " ", "   ", "\t", "\n");
    }

    private static Segmento primeiroSegmentoDisponivel() {
        Segmento[] valores = Segmento.values();
        if (valores.length == 0) {
            throw new IllegalStateException("O enum Segmento precisa ter ao menos um valor para os testes.");
        }
        return valores[0];
    }
}