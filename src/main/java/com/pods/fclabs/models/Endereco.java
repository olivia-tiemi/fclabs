package com.pods.fclabs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@ApiModel(value = "Objeto Endereco",subTypes = {Endereco.class})
@Data
@Entity
@Table(name = "TB_ENDERECO")
public class Endereco {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @ApiModelProperty(dataType = "String", example = "CEP", required = true, position = 1)
    private String cep;
    @ApiModelProperty(dataType = "Integer", example = "Numero", required = true, position = 2)
    private Integer numero;
    @ApiModelProperty(dataType = "String", example = "Complemento", position = 3)
    private String complemento;

    @ApiModelProperty(hidden = true)
    private Date dtCriacao;
    @ApiModelProperty(hidden = true)
    private Date dtUltAlteracao;

    @ManyToOne @JsonIgnore
    private Usuario usuario;
}
