package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.service.PetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService service;

    @Test
    @DisplayName("Deve listar os pets disponíveis")
    void listarPetsDisponiveis() throws Exception {

        //ACT
        var response = mockMvc.perform(
                get("/pets").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        then(service).should().buscarPetsDisponiveis();
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

}