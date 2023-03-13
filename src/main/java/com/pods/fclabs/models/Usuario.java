package com.pods.fclabs.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Objeto Usuario",subTypes = {Usuario.class})
@Data
@Entity
@Table(name = "TB_USUARIO")
public class Usuario implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @ApiModelProperty(dataType = "String", example = "Nome do Usuario X", required = true, position = 1)
    private String nome;
    @ApiModelProperty(dataType = "String", example = "Nome da mãe do Usuario X", required = true, position = 3)
    private String nomeMae;
    @ApiModelProperty(hidden = true)
    private Date dtCriacao;
    @ApiModelProperty(hidden = true)
    private Date dtUltAlteracao;
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "usuario")
    private List<Endereco> enderecos = new ArrayList<>();
}
