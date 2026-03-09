package br.com.denram.empreendimentos_sc_api.service;

import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoRequestDto;
import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import br.com.denram.empreendimentos_sc_api.exception.ResourceNotFoundException;
import br.com.denram.empreendimentos_sc_api.repository.EmpreendimentoRepository;
import br.com.denram.empreendimentos_sc_api.repository.MunicipioRepository;
import br.com.denram.empreendimentos_sc_api.specification.EmpreendimentoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpreendimentoService {

    private final EmpreendimentoRepository empreendimentoRepository;
    private final MunicipioRepository municipioRepository;

    @Transactional
    public Empreendimento criar(EmpreendimentoRequestDto dto) {
        Municipio municipio = buscarMunicipioExistente(dto.getMunicipioId());

        Empreendimento empreendimento = new Empreendimento();
        preencherDados(empreendimento, dto, municipio);

        return empreendimentoRepository.save(empreendimento);
    }

    @Transactional
    public Empreendimento atualizar(Long id, EmpreendimentoRequestDto dto) {
        Empreendimento empreendimento = buscarEmpreendimentoExistente(id);
        Municipio municipio = buscarMunicipioExistente(dto.getMunicipioId());

        preencherDados(empreendimento, dto, municipio);

        return empreendimentoRepository.save(empreendimento);
    }

    @Transactional(readOnly = true)
    public Empreendimento buscarPorId(Long id) {
        return buscarEmpreendimentoExistente(id);
    }

    @Transactional(readOnly = true)
    public Page<Empreendimento> buscar(String nome,
                                       String municipioNome,
                                       String segmento,
                                       Pageable pageable) {
        Segmento segmentoEnum = Segmento.fromNome(segmento);

        Specification<Empreendimento> specification = Specification
                .where(EmpreendimentoSpecification.nomeContendo(nome))
                .and(EmpreendimentoSpecification.municipioNomeContendo(municipioNome))
                .and(EmpreendimentoSpecification.segmentoIgual(segmentoEnum));

        return empreendimentoRepository.findAll(specification, pageable);
    }

    @Transactional
    public void excluir(Long id) {
        Empreendimento empreendimento = buscarEmpreendimentoExistente(id);
        empreendimentoRepository.delete(empreendimento);
    }

    private Empreendimento buscarEmpreendimentoExistente(Long id) {
        return empreendimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empreendimento não encontrado para o id: " + id));
    }

    private Municipio buscarMunicipioExistente(Long municipioId) {
        return municipioRepository.findById(municipioId)
                .orElseThrow(() -> new ResourceNotFoundException("Município não encontrado para o id: " + municipioId));
    }

    private void preencherDados(Empreendimento empreendimento, EmpreendimentoRequestDto dto, Municipio municipio) {
        empreendimento.setNome(dto.getNome());
        empreendimento.setNomeResponsavel(dto.getNomeResponsavel());
        empreendimento.setMunicipio(municipio);
        empreendimento.setSegmento(Segmento.fromNome(dto.getSegmento()));
        empreendimento.setEmail(dto.getEmail());
        empreendimento.setTelefone(dto.getTelefone());
        empreendimento.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : Boolean.TRUE);
    }
}