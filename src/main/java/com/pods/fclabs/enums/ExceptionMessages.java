package com.pods.fclabs.enums;

public enum ExceptionMessages {
    USUARIO_EXISTENTE_EXCEPTION("UsuarioExistenteException"),
    USUARIO_INEXISTENTE_EXCEPTION("UsuarioInexistenteException"),
    CAMPO_OBRIGATORIO_EXCEPTION("CampoObrigatorioException"),
    CEP_INVALIDO_EXCEPTION("CepInvalidoException"),
    ENDERECO_INEXISTENTE_EXCEPTION("EnderecoInexistenteException");

    private final String mensagem;

    ExceptionMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
