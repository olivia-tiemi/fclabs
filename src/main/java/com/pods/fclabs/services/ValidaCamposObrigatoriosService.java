package com.pods.fclabs.services;

import java.util.UUID;

import com.pods.fclabs.models.EnderecoForm;
import org.springframework.stereotype.Service;

import com.pods.fclabs.exception.CampoObrigatorioException;
import com.pods.fclabs.models.Usuario;

@Service
public class ValidaCamposObrigatoriosService {
    public void validaCamposObrigatoriosUsuario(Usuario usuario) {
        validaCampo(usuario.getNome(), "Campo nome é obrigatório para cadastro de Usuario!");
        validaCampo(usuario.getNomeMae(), "Campo Nome Mãe é obrigatório para cadastro de Usuario!");
    }
    public void validaCamposObrigatoriosEndereco(EnderecoForm endereco) {
        validaCampo(endereco.getCep(), "Campo CEP é obrigatório para cadastro de Endereco!");
        validaCampo(endereco.getNumero(), "Campo Número é obrigatório para cadastro de Endereco!");
    }
    public void validaIdUsuario(UUID id) {
        validaCampo(id, "Informar o ID do Usuario");
    }

    private static void validaCampo(Object campo, String mensagemException) {
        if (java.util.Objects.isNull(campo) || campo.toString().isEmpty()) {
            throw new CampoObrigatorioException(mensagemException);
        }
    }
}