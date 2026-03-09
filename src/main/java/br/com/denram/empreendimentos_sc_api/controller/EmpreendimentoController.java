package br.com.denram.empreendimentos_sc_api.controller;

import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoRequestDto;
import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoResponseDto;
import br.com.denram.empreendimentos_sc_api.mapper.EmpreendimentoMapper;
import br.com.denram.empreendimentos_sc_api.service.EmpreendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empreendimentos")
@RequiredArgsConstructor
public class EmpreendimentoController {

    private final EmpreendimentoService empreendimentoService;
    private final EmpreendimentoMapper empreendimentoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpreendimentoResponseDto criar(@Valid @RequestBody EmpreendimentoRequestDto requestDto) {
        return empreendimentoMapper.toResponseDto(empreendimentoService.criar(requestDto));
    }

    @PutMapping("/{id}")
    public EmpreendimentoResponseDto atualizar(@PathVariable Long id,
                                               @Valid @RequestBody EmpreendimentoRequestDto requestDto) {
        return empreendimentoMapper.toResponseDto(empreendimentoService.atualizar(id, requestDto));
    }

    @GetMapping("/{id}")
    public EmpreendimentoResponseDto buscarPorId(@PathVariable Long id) {
        return empreendimentoMapper.toResponseDto(empreendimentoService.buscarPorId(id));
    }

    @GetMapping
    public Page<EmpreendimentoResponseDto> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String municipioNome,
            @RequestParam(required = false) String segmento,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return empreendimentoService.buscar(nome, municipioNome, segmento, pageable)
                .map(empreendimentoMapper::toResponseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        empreendimentoService.excluir(id);
    }
}