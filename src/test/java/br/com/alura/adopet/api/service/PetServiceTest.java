package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository repository;

    @Mock
    private Abrigo abrigo;

    @Mock
    private CadastroPetDto dto;

    @Mock
    private Pet pet;

    @InjectMocks
    private PetService petService;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    @DisplayName("Deve salvar um pet referente ao abrigo informado")
    void cenario01() {

        //ACT
        petService.cadastrarPet(abrigo,dto);

        //ASSERT
        then(repository).should().save(petCaptor.capture());

        var petSalvo = petCaptor.getValue();

        Assertions.assertThat(petSalvo.getAbrigo()).isEqualTo(abrigo);

    }

    @Test
    @DisplayName("Deve chamar o método findAllByAdotadoFalse do repositorio")
    void cenario02() {
        //act
        petService.buscarPetsDisponiveis();

        //assert
        then(repository).should().findAllByAdotadoFalse();
    }

}