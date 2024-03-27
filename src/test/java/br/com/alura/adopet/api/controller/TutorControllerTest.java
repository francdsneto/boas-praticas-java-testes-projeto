package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.TutorService;
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

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CadastroTutorDto> jsonCadastroTutorDto;

    @Autowired
    private JacksonTester<AtualizacaoTutorDto> jsonAtualizacaoTutorDto;

    private CadastroTutorDto cadastroTutorDto;

    private AtualizacaoTutorDto atualizacaoTutorDto;

    @MockBean
    private TutorService tutorService;

    @Test
    @DisplayName("Cadastra um tutor com sucesso e retorna o código 200")
    void cadastrarTutorComSucesso() throws Exception {

        //ARRANGE
        cadastroTutorDto = new CadastroTutorDto("nome", "85999999999", "email@email.com");
        var json = jsonCadastroTutorDto.write(cadastroTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer devido a nome inválido e deve retornar código 400.")
    void deveCausarExceptionDevidoANomeInvalido() throws Exception {

        //ARRANGE
        cadastroTutorDto = new CadastroTutorDto("", "85999999999", "email@email.com");
        var json = jsonCadastroTutorDto.write(cadastroTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer devido a telefone inválido e deve retornar código 400.")
    void deveCausarExceptionDevidoATelefoneInvalido() throws Exception {

        //ARRANGE
        cadastroTutorDto = new CadastroTutorDto("nome", "", "email@email.com");
        var json = jsonCadastroTutorDto.write(cadastroTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer devido a email inválido e deve retornar código 400.")
    void deveCausarExceptionDevidoAEmailInvalido() throws Exception {

        //ARRANGE
        cadastroTutorDto = new CadastroTutorDto("nome", "85999999999", "");
        var json = jsonCadastroTutorDto.write(cadastroTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve causar uma excessão simulando o retorno a já existencia do tutor no banco, e retornar o código 400.")
    void deveCausarExceptionDevidoATutorJaExistente() throws Exception {

        //ARRANGE
        cadastroTutorDto = new CadastroTutorDto("nome", "85999999999", "");
        var json = jsonCadastroTutorDto.write(cadastroTutorDto).getJson();
        doThrow(new ValidacaoException("")).when(tutorService).cadastrar(cadastroTutorDto);

        //ACT
        var response = mockMvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve atualizar um tutor com sucesso e retornar código 200")
    void atualizaTutorComSucesso() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(1l, "nome", "85999999999", "email@email.com");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Não deve atualizar devido a parametro id inválido e deve retornar erro 400.")
    void naoDeveAtualizarDevidoAoParametroIdInvalido() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(null, "nome", "85999999999", "email@email.com");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Não deve atualizar devido a parametro nome inválido e deve retornar erro 400.")
    void naoDeveAtualizarDevidoAoParametroNomeInvalido() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(1l, "", "85999999999", "email@email.com");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Não deve atualizar devido a parametro telefone inválido e deve retornar erro 400.")
    void naoDeveAtualizarDevidoAoParametroTelefoneInvalido() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(1l, "nome", "", "email@email.com");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Não deve atualizar devido a parametro email inválido e deve retornar erro 400.")
    void naoDeveAtualizarDevidoAoParametroEmailInvalido() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(1l, "nome", "85999999999", "");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve simular uma excessão vinda do serviço e retornar erro 400.")
    void simulaExceptionVindaDoServicoERetornaErro400() throws Exception {

        //ARRANGE
        atualizacaoTutorDto = new AtualizacaoTutorDto(1l, "nome", "85999999999", "");
        var json = jsonAtualizacaoTutorDto.write(atualizacaoTutorDto).getJson();
        doThrow(new ValidacaoException("")).when(tutorService).atualizar(atualizacaoTutorDto);

        //ACT
        var response = mockMvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

}