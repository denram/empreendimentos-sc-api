package br.com.denram.empreendimentos_sc_api.converter;

import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SegmentoConverter implements AttributeConverter<Segmento, Short> {

    @Override
    public Short convertToDatabaseColumn(Segmento attribute) {
        return attribute != null ? attribute.getIndex() : null;
    }

    @Override
    public Segmento convertToEntityAttribute(Short dbData) {
        return dbData != null ? Segmento.fromIndex(dbData) : null;
    }
}