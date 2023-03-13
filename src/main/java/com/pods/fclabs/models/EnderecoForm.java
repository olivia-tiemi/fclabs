package com.pods.fclabs.models;

import lombok.Data;

import java.util.UUID;
@Data
public class EnderecoForm {
    private String cep;
    private Integer numero;
    private String complemento;
    private UUID usuarioId;
}
