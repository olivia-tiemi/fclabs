package com.pods.fclabs.controllers;

import com.google.gson.Gson;
import com.pods.fclabs.enums.ExceptionMessages;
import com.pods.fclabs.enums.LoggerInfoLevelEnum;
import com.pods.fclabs.exception.CampoObrigatorioException;
import com.pods.fclabs.exception.CepInvalidoException;
import com.pods.fclabs.exception.EnderecoInexistenteException;
import com.pods.fclabs.exception.UsuarioInexistenteException;
import com.pods.fclabs.models.Endereco;
import com.pods.fclabs.models.EnderecoForm;
import com.pods.fclabs.models.Usuario;
import com.pods.fclabs.services.EnderecoService;
import com.pods.fclabs.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/endereco")
@Api(value = "Endereço", description = "Controle de Endereço dos Usuários")
public class EnderecoController {
    private static final String PATH_MAPPING = "/endereco";
    private final EnderecoService service;
    public EnderecoController(EnderecoService service){
        this.service = service;
    }
    private final Gson gson = new Gson();

    @ApiOperation(value = "salva", nickname = "Salvar Endereço")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> salva(@RequestBody @Valid EnderecoForm enderecoForm) {
        try {
            final Endereco novoEndereco = service.salva(enderecoForm);
            return new ResponseEntity<>(novoEndereco, HttpStatus.CREATED);
        } catch (CampoObrigatorioException coe) {
            Util.registraLog(this.getClass(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), "salva", coe.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), coe.getMessage(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (CepInvalidoException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.CEP_INVALIDO_EXCEPTION.getMensagem(), "salva", e.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.CEP_INVALIDO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (UsuarioInexistenteException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), "salva", e.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaFindAll(@RequestParam(required = false) UUID usuarioId) {
        try {
            final List<Endereco> enderecos;
            if(usuarioId == null) {
                enderecos = service.findAll();
            } else {
                enderecos = service.findByUsuarioId(usuarioId);
            }
            return Objects.isNull(enderecos) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(enderecos, HttpStatus.OK);
        } catch (UsuarioInexistenteException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), "salva", e.getMessage(), usuarioId, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> busca(@PathVariable UUID id) {
        try {
            final Endereco endereco = service.findByIdEndereco(id);
            return new ResponseEntity<>(endereco, HttpStatus.OK);
        } catch (EnderecoInexistenteException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), "salva", e.getMessage(), id, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.ENDERECO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/cep/{cep}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaCep(@PathVariable String cep) {
        try {
            return ResponseEntity.ok(gson.toJson(service.buscaCep(cep)));
        } catch (CepInvalidoException e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Usuario(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.CEP_INVALIDO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "atualiza", nickname = "Atualizar Endereço")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualiza(@PathVariable UUID id, @RequestBody EnderecoForm enderecoForm) {
        try {
            return new ResponseEntity<>(service.atualiza(id, enderecoForm), HttpStatus.OK);
        } catch (CampoObrigatorioException coe) {
            Util.registraLog(this.getClass(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), "salva", coe.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), coe.getMessage(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (CepInvalidoException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.CEP_INVALIDO_EXCEPTION.getMensagem(), "salva", e.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.CEP_INVALIDO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (UsuarioInexistenteException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), "salva", e.getMessage(), enderecoForm, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), e.getMessage(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> desativa(@PathVariable UUID id) {
        try {
            service.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EnderecoInexistenteException pie) {
            Util.registraLog(this.getClass(), ExceptionMessages.ENDERECO_INEXISTENTE_EXCEPTION.getMensagem(), "atualiza", pie.getMessage(), id, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(), pie.getMessage(), ExceptionMessages.ENDERECO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Endereco(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
