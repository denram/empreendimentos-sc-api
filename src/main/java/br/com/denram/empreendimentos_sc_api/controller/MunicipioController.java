package br.com.denram.empreendimentos_sc_api.controller;

import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.dto.MunicipioResponseDto;
import br.com.denram.empreendimentos_sc_api.mapper.MunicipioMapper;
import br.com.denram.empreendimentos_sc_api.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/municipios")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;

    @GetMapping
    public Page<MunicipioResponseDto> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Municipio> municipios = municipioRepository.findAll(pageable);
        return municipioMapper.toDtoPage(municipios);
    }

    @GetMapping("/search")
    public Page<MunicipioResponseDto> buscarPorNome(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Municipio> municipios = municipioRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return municipioMapper.toDtoPage(municipios);
    }
}