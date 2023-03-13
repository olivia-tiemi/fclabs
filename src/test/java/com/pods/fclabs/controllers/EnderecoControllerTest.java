package com.pods.fclabs.controllers;

import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnderecoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static UUID usuarioId;
    private static UUID enderecoId;
    @BeforeAll
    public void setupUsuario() {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO TB_USUARIO (dt_criacao, dt_ult_alteracao, nome, nome_mae, id) VALUES (?, ?, ?, ?, ?)",
                new Object[]{now, now, "joana", "mãe da joana", uuid});
        usuarioId = uuid;
    }
    @BeforeAll
    public void setupEndereco() {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO tb_endereco (cep, complemento, dt_criacao, dt_ult_alteracao, numero, usuario_id, id) values (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"01012030", null, now, now, "345", usuarioId, uuid});
        enderecoId = uuid;
    }
    @Test
    @Order(1)
    void deveriaDevolverStatus201Created() throws Exception {
        URI uri = new URI("/endereco");
        String json = "{\"cep\":\"01012030\", \"numero\": \"345\", \"usuarioId\": \"" + usuarioId + "\"}";
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(201));
    }

    @Test
    @Order(2)
    void deveriaDevolverCampoObrigatorioException() throws Exception {
        URI uri = new URI("/endereco");
        String json = "{\"cep\":\"\", \"numero\": \"345\", \"usuarioId\": \"23f3e245-8a35-487b-938b-014e694eefe7\"}";
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CampoObrigatorioException"));
    }

    @Test
    @Order(3)
    void deveriaDevolverCepInvalidoException() throws Exception {
        URI uri = new URI("/endereco");
        String json = "{\"cep\":\"0101200\", \"numero\": \"345\", \"usuarioId\": \"23f3e245-8a35-487b-938b-014e694eefe7\"}";
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CepInvalidoException"));
    }

    @Test
    @Order(4)
    void deveriaDevolverUsuarioInexistenteException() throws Exception {
        URI uri = new URI("/endereco");
        String json = "{\"cep\":\"01012030\", \"numero\": \"345\", \"usuarioId\": \"23f3e245-8a35-487b-938b-014e694eefe9\"}";
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("UsuarioInexistenteException"));
    }

    @Test
    @Order(5)
    void buscaFindAll() throws Exception {
        URI uri = new URI("/endereco/");
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200));
    }

    @Test
    @Order(6)
    void busca() throws Exception{
        URI uri = new URI("/endereco/" + enderecoId);
        mockMvc.perform(MockMvcRequestBuilders
                .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cep").value("01012030"));
    }
}