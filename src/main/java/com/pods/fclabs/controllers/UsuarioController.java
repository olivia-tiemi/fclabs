package com.pods.fclabs.controllers;


import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;

import com.pods.fclabs.enums.ErrorMessages;
import com.pods.fclabs.enums.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pods.fclabs.enums.LoggerInfoLevelEnum;
import com.pods.fclabs.exception.CampoObrigatorioException;
import com.pods.fclabs.exception.UsuarioExistenteException;
import com.pods.fclabs.exception.UsuarioInexistenteException;
import com.pods.fclabs.models.Usuario;
import com.pods.fclabs.models.UsuarioResponse;
import com.pods.fclabs.services.UsuarioService;
import com.pods.fclabs.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/usuario")
@Api(value = "Usuario", description = "Controle de  Usuarios")
public class UsuarioController {

    private static final String PATH_MAPPING = "/usuario";
        
    @Autowired
    private UsuarioService service;

    private Gson gson = new Gson();

    @ApiOperation(value = "salva", nickname = "Salvar Usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = UsuarioResponse.class, message = ""),
            @ApiResponse(code = 400, message = "Obrigatório enviar os dados para cadastro conforme documentacão do Swagger"),
            @ApiResponse(code = 409, message = "Já existe um usuario com estes dados"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure", response = Exception.class)})
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> salva(@RequestBody @Valid Usuario usuario) throws Exception {
        try {
            if (Objects.isNull(usuario)) {
                Util.registraLog(this.getClass(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), "salva", ErrorMessages.DADOS_OBRIGATORIOS.getMensagem(), usuario, LoggerInfoLevelEnum.ERROR);

                return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.CONFLICT.name(),
                        ErrorMessages.DADOS_OBRIGATORIOS.getMensagem(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
            }
            final UsuarioResponse usuarioResponse =   service.salva(usuario);

            return Objects.isNull(usuarioResponse) ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
        } catch (CampoObrigatorioException coe) {
            Util.registraLog(this.getClass(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), "salva", coe.getMessage(), usuario, LoggerInfoLevelEnum.ERROR);

            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(),
                    coe.getMessage(), ExceptionMessages.CAMPO_OBRIGATORIO_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        } catch (UsuarioExistenteException e) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_EXISTENTE_EXCEPTION.getMensagem(), "salva", ErrorMessages.USUARIO_EXISTENTE.getMensagem(), usuario, LoggerInfoLevelEnum.ERROR);

            return new ResponseEntity<>(Util.criaMsgRetornoComUsuario(HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.name(),
                    ErrorMessages.USUARIO_EXISTENTE.getMensagem(), ExceptionMessages.USUARIO_EXISTENTE_EXCEPTION.getMensagem(), gson.fromJson(e.getMessage(), Usuario.class), PATH_MAPPING), HttpStatus.CONFLICT);
        } 
    }    
    
        
    @ApiOperation(value = "atualiza", nickname = "Atualizar Usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 204, response = UsuarioResponse.class, message = ""),
            @ApiResponse(code = 400, message = "Obrigatório enviar os dados para cadastro conforme documentacão do Swagger"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure", response = Exception.class)})
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualiza(@RequestBody Usuario usuario) {
        try {
            return  new ResponseEntity<>(service.atualiza(usuario), HttpStatus.OK);
        } catch (NullPointerException e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "atualiza", e.getStackTrace().toString(), usuario, LoggerInfoLevelEnum.INFO);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (UsuarioInexistenteException pie) {
            Util.registraLog(this.getClass(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), "atualiza", pie.getMessage(), usuario, LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(Util.criaMsgRetorno(HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.name(),
                    pie.getMessage(), ExceptionMessages.USUARIO_INEXISTENTE_EXCEPTION.getMensagem(), PATH_MAPPING), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> desativa(@PathVariable UUID id) {
        try {
            service.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "Remove", e.getStackTrace().toString(), new Usuario(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> busca(@PathVariable UUID id) {
        try {
            final UsuarioResponse usuarioResponse = service.findbyidUsuario(id);

            if (Objects.isNull(usuarioResponse))
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Usuario(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaFindAll() {
        try {
            final List<UsuarioResponse> usuarioResponse = service.findAll();

            if (Objects.isNull(usuarioResponse))
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            Util.registraLog(this.getClass(), e.getClass().getName(), "busca", e.getStackTrace().toString(), new Usuario(), LoggerInfoLevelEnum.ERROR);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
    
    
}
