package com.pods.fclabs.enums;

public enum ErrorMessages {
    DADOS_OBRIGATORIOS("Obrigatório enviar os dados para cadastro conforme documentacão do Swagger"),
    USUARIO_EXISTENTE("Já existe um usuario com estes dados");

    private final String mensagem;

    ErrorMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
