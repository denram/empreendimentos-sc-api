package br.com.denram.empreendimentos_sc_api.domain;

import java.util.Arrays;

public enum Segmento {

    TECNOLOGIA((short) 0),
    COMERCIO((short) 1),
    INDUSTRIA((short) 2),
    SERVICOS((short) 3),
    AGRONEGOCIO((short) 4);

    private final short index;

    Segmento(short index) {
        this.index = index;
    }

    public short getIndex() {
        return index;
    }

    public static Segmento fromIndex(Short index) {
        if (index == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(segmento -> segmento.index == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Segmento inválido: " + index));
    }

    public static Segmento fromNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(segmento -> segmento.name().equalsIgnoreCase(nome.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Segmento inválido: " + nome));
    }
}