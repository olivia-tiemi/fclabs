package com.pods.fclabs.services;

import com.pods.fclabs.enums.ErrorMessages;
import com.pods.fclabs.enums.ExceptionMessages;
import com.pods.fclabs.enums.LoggerInfoLevelEnum;
import com.pods.fclabs.exception.CepInvalidoException;
import com.pods.fclabs.exception.UsuarioInexistenteException;
import com.pods.fclabs.models.Endereco;
import com.pods.fclabs.models.EnderecoForm;
import com.pods.fclabs.models.Usuario;
import com.pods.fclabs.repositories.EnderecoRepository;
import com.pods.fclabs.repositories.UsuarioRepository;
import com.pods.fclabs.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final CepService cepService;
    private final ValidaCamposObrigatoriosService validaCamposObrigatoriosService;
    private final Util util;

    public EnderecoService(EnderecoRepository enderecoRepository, CepService cepService,
                           ValidaCamposObrigatoriosService validaCamposObrigatoriosService, Util util) {
        this.enderecoRepository = enderecoRepository;
        this.cepService = cepService;
        this.validaCamposObrigatoriosService = validaCamposObrigatoriosService;
        this.util = util;
    }

    public Endereco salva(EnderecoForm endereco) {
        validaCamposObrigatoriosService.validaIdUsuario(endereco.getUsuarioId());
        validaCamposObrigatoriosService.validaCamposObrigatoriosEndereco(endereco);
        cepService.validaCep(endereco.getCep());
        Usuario usuario = util.encontraUsuario(endereco.getUsuarioId());
        Endereco novoEndereco = util.converteEndereco(endereco, usuario);
        enderecoRepository.save(novoEndereco);
        return novoEndereco;
    }

    public Object buscaCep(String cep) throws CepInvalidoException {
        try {
            return cepService.validaCep(cep);
        } catch (CepInvalidoException e) {
            throw e;
        }
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Endereco findByIdEndereco(UUID id) {
        return util.encontraEndereco(id);
    }

    public Endereco atualiza(UUID id, EnderecoForm endereco) {
        validaCamposObrigatoriosService.validaIdUsuario(endereco.getUsuarioId());
        validaCamposObrigatoriosService.validaCamposObrigatoriosEndereco(endereco);
        cepService.validaCep(endereco.getCep());
        Usuario usuario = util.encontraUsuario(endereco.getUsuarioId());
        Endereco enderecoAtualizado = util.atualizaEndereco(endereco, util.encontraEndereco(id), usuario);
        enderecoRepository.save(enderecoAtualizado);
        return enderecoAtualizado;
    }

    public void remove(UUID id) {
        Endereco endereco = util.encontraEndereco(id);
        enderecoRepository.delete(endereco);
    }

    public List<Endereco> findByUsuarioId(UUID usuarioId) {
        Usuario usuario = util.encontraUsuario(usuarioId);
        List<Endereco> enderecos = enderecoRepository.findByUsuario(usuario);
        return enderecos;
    }
}
