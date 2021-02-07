package br.com.vfsilva.jwt.api.app.converter;

public interface ConverterDTO<MODEL, DTO> {

    MODEL toModel(DTO dto);

    DTO toDTO(MODEL dto);
}
