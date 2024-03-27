package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AbrigoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Mock
    private Pet pet;

    @Spy
    private List<PetDto> listaPets = new ArrayList<>();

    private CadastroAbrigoDto dto;

    @Autowired
    private JacksonTester<CadastroAbrigoDto> jsonCadastroAbrigoDTO;

    @Autowired
    private JacksonTester<CadastroPetDto> jsonCadastroPetDto;

    private CadastroPetDto cadastroPetDto;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Test
    @DisplayName("O cadastro deve ocorrer e retornar código 200")
    void cadastrarSucesso() throws Exception {

        //ARRANGE
        this.dto = new CadastroAbrigoDto("nome", "85999999999", "email@mail.com");

        var json = jsonCadastroAbrigoDTO.write(this.dto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/abrigos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer e retornar código 400 por nome inválido")
    void cadastrarErro400PorNomeInvalido() throws Exception {

        //ARRANGE
        this.dto = new CadastroAbrigoDto("", "85999999999", "email@mail.com");

        var json = jsonCadastroAbrigoDTO.write(this.dto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer e retornar código 400 por telefone inválido")
    void cadastrarErro400PorTelefoneInvalido() throws Exception {

        //ARRANGE
        this.dto = new CadastroAbrigoDto("nome", "", "email@mail.com");

        var json = jsonCadastroAbrigoDTO.write(this.dto).getJson();

        //ACT
        var response = mockMvc.perform(
                post("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("O cadastro não deve ocorrer e retornar código 400 por email inválido")
    void cadastrarErro400PorEmailInvalido() throws Exception {

        //ARRANGE
        this.dto = new CadastroAbrigoDto("nome", "85999999999", "email@mail.");

        var json = jsonCadastroAbrigoDTO.write(this.dto).getJson();

        doThrow(new ValidacaoException("Dados já cadastrados para outro abrigo!")).when(abrigoService).cadastrar(any(CadastroAbrigoDto.class));

        //ACT
        var response = mockMvc.perform(
                post("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve retornar uma lista de pets passando com o código 200 recebendo um long como parametro")
    void deveRetornarUmaListaDePetsECodigo200RecebendoLongComoParametro() throws Exception {

        //ARRANGE
        var param = 1l;

        listaPets.add(new PetDto(pet));
        when(abrigoService.carregarAbrigo(String.valueOf(param))).thenReturn(abrigo);
        when(abrigoService.listarPetsDoAbrigo(String.valueOf(param))).thenReturn(listaPets);

        //ACT
        var response = mockMvc.perform(
                get("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve retornar uma lista de pets passando com o código 200 recebendo uma nome válido como parametro")
    void deveRetornarUmaListaDePetsECodigo200RecebendoNomeComoParametro() throws Exception {

        //ARRANGE
        var param = "nome";

        listaPets.add(new PetDto(pet));
        when(abrigoService.carregarAbrigo(param)).thenReturn(abrigo);
        when(abrigoService.listarPetsDoAbrigo(param)).thenReturn(listaPets);

        //ACT
        var response = mockMvc.perform(
                get("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve lançar uma excessão por parametro inválido e retornar código 404 not found")
    void deveRetornarUmaExcessaoPorParametroInvalidoERetornarErro404() throws Exception {

        //ARRANGE
        var param = "";

        listaPets.add(new PetDto(pet));
        when(abrigoService.listarPetsDoAbrigo(param)).thenReturn(listaPets);

        doThrow(new ValidacaoException("")).when(abrigoService).carregarAbrigo(param);

        //ACT
        var response = mockMvc.perform(
                get("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERTION
        Assertions.assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(response.getStatus());

    }

    @Test
    @DisplayName("Deve cadastrar o pet corretamente no abrigo e retornar código 200")
    void deveCadastrarPetNoAbrigoCorretamente() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Bartolomeu",
                "Bulldog",
                1,
                "Cinza",
                4.0f
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    @DisplayName("Não deve cadastrar o pet no abrigo e retornar código 400 por nome inválido")
    void naoDeveCadastrarPetNoAbrigoEDeveRetornarCodigo400PorNomeInvalido() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "",
                "Bulldog",
                1,
                "Cinza",
                4.0f
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    @DisplayName("Não deve cadastrar o pet no abrigo e retornar código 400 por raça inválida")
    void naoDeveCadastrarPetNoAbrigoEDeveRetornarCodigo400PorRacaInvalida() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Bartolomeu",
                "",
                1,
                "Cinza",
                4.0f
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    @DisplayName("Não deve cadastrar o pet no abrigo e retornar código 400 por idade inválida")
    void naoDeveCadastrarPetNoAbrigoEDeveRetornarCodigo400PorIdadeInvalida() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Bartolomeu",
                "Bulldog",
                null,
                "Cinza",
                4.0f
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    @DisplayName("Não deve cadastrar o pet no abrigo e retornar código 400 por cor inválida")
    void naoDeveCadastrarPetNoAbrigoEDeveRetornarCodigo400PorCorInvalida() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Bartolomeu",
                "Bulldog",
                1,
                "",
                4.0f
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    @DisplayName("Não deve cadastrar o pet no abrigo e retornar código 400 por peso inválido")
    void naoDeveCadastrarPetNoAbrigoEDeveRetornarCodigo400PorPesoInvalido() throws Exception {

        //ARRANGE
        var param = 1l;
        cadastroPetDto = new CadastroPetDto(
                TipoPet.CACHORRO,
                "Bartolomeu",
                "Bulldog",
                1,
                "Cinza",
                null
        );
        var json = jsonCadastroPetDto.write(cadastroPetDto).getJson();

        //ACT

        var response = mockMvc.perform(
                post("/abrigos/"+param+"/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();


        //ASSERT
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

}