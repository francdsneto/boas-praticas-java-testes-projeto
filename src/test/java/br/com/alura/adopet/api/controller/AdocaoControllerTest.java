package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdocaoService adocaoService;

    @Autowired
    private JacksonTester<AprovacaoAdocaoDto> jsonAprovacaoDTO;

    @Autowired
    private JacksonTester<ReprovacaoAdocaoDto> jsonReprovacaoDTO;


    @Test
    void deveriaDevolverCodigo400ParaSolicitacaoDeAdocaoComErros() throws Exception {

        //ARRANGE

        String json = "{}";

        //ACT

        var response = mockMvc.perform(
                post("/adocoes")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(400).isEqualTo(response.getStatus());

    }

    @Test
    void deveriaDevolverCodigo200ParaSolicitacaoDeAdocaoSemErros() throws Exception {

        //ARRANGE

        String json = """
                    {
                        "idPet" : 1,
                        "idTutor" : 1,
                        "motivo" : "Motivo qualquer"
                    }
                """;

        //ACT

        var response = mockMvc.perform(
                post("/adocoes")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(200).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve aprovar a adoção e retornar o código 200")
    void deveAprovarAAdocao() throws Exception {

        //ARRANGE
        AprovacaoAdocaoDto aprovacaoDTO = new AprovacaoAdocaoDto(1l);
        String json = jsonAprovacaoDTO.write(aprovacaoDTO).getJson();


        //ACT

        var response = mockMvc.perform(
                put("/adocoes/aprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Não deve aprovar a adoção e retornar o código 400")
    void naoDeveAprovarAAdocao() throws Exception {

        //ARRANGE
        AprovacaoAdocaoDto aprovacaoDTO = new AprovacaoAdocaoDto(null);
        String json = jsonAprovacaoDTO.write(aprovacaoDTO).getJson();


        //ACT

        var response = mockMvc.perform(
                put("/adocoes/aprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve reprovar a adoção e retornar o código 200")
    void deveReprovarAAdocao() throws Exception {

        //ARRANGE
        ReprovacaoAdocaoDto dto = new ReprovacaoAdocaoDto(1l, "Qualquer justificativa");
        String json = jsonReprovacaoDTO.write(dto).getJson();


        //ACT

        var response = mockMvc.perform(
                put("/adocoes/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("A reprovação deve dar erro e retornar código 400 por id inválido")
    void deveRetornarErro400PorIdInvalido() throws Exception {

        //ARRANGE
        ReprovacaoAdocaoDto dto = new ReprovacaoAdocaoDto(null, "Motivo qualquer");
        String json = jsonReprovacaoDTO.write(dto).getJson();


        //ACT

        var response = mockMvc.perform(
                put("/adocoes/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("A reprovação deve dar erro e retornar código 400 por motivo inválido")
    void deveRetornarErro400PorMotivoInvalido() throws Exception {

        //ARRANGE
        ReprovacaoAdocaoDto dto = new ReprovacaoAdocaoDto(1l, "");
        String json = jsonReprovacaoDTO.write(dto).getJson();


        //ACT

        var response = mockMvc.perform(
                put("/adocoes/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT

        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

}