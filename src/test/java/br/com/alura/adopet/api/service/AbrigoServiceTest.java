package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @Mock
    private CadastroAbrigoDto dto;

    @Mock
    private Abrigo abrigo;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private AbrigoService abrigoService;


    @Test
    @DisplayName("Deve chamar o método findAll do repository")
    void chamaFindAll() {

        abrigoService.listar();

        then(abrigoRepository).should().findAll();

    }

    @Test
    @DisplayName("Deve chamar a lista de pets do abrigo através do nome")
    void deveChamarAListaDePetsDoAbrigoAtravesDoNome() {

        //arrange

        var nome = "Miau";
        given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

        //act
        abrigoService.listarPetsDoAbrigo(nome);

        //assert
        then(petRepository).should().findByAbrigo(abrigo);

    }

    @Test
    @DisplayName("Deve chamar a lista de pets do abrigo através do id")
    void deveChamarAListaDePetsDoAbrigoAtravesDoId() {

        //arrange

        var id = 1l;
        given(abrigoRepository.findById(id)).willReturn(Optional.of(abrigo));

        //act
        abrigoService.listarPetsDoAbrigo(String.valueOf(id));

        //assert
        then(petRepository).should().findByAbrigo(abrigo);

    }

    @Test
    @DisplayName("Deve permitir o cadastro pois o abrigo ainda não foi cadastrado")
    void cenario01() {

        //ARRANGE
        when(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).thenReturn(false);

        //ACT
        abrigoService.cadastrar(dto);

        //ASSERT
        then(abrigoRepository).should().save(new Abrigo(dto));

    }

    @Test
    @DisplayName("Deve impedir o cadastro pois o abrigo já foi cadastrado")
    void cenario02() {

        //ARRANGE
        when(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).thenReturn(true);

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.cadastrar(dto));

    }

    @Test
    @DisplayName("Deve realizar uma pesquisa por id pois foi passado um valor numérico")
    void cenario03() {

        //ARRANGE
        var valor = abrigo.getId();
        when(abrigoRepository.findById(valor)).thenReturn(Optional.of(abrigo));

        //ACT
        abrigoService.carregarAbrigo(String.valueOf(valor));

        //ASSERT
        then(abrigoRepository).should().findById(valor);

    }

    @Test
    @DisplayName("Deve realizar uma pesquisa por nome pois foi passado um valor de texto")
    void cenario04() {

        //ARRANGE
        var valor = abrigo.getNome();
        when(abrigoRepository.findByNome(valor)).thenReturn(Optional.of(abrigo));

        //ACT
        abrigoService.carregarAbrigo(valor);

        //ASSERT
        then(abrigoRepository).should().findByNome(valor);

    }

    @Test
    @DisplayName("Deve lançar excessão pois o resultado foi nulo")
    void cenario05() {

        //ARRANGE
        when(abrigoRepository.findByNome(any())).thenReturn(Optional.empty());

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.carregarAbrigo(any()));

    }


}