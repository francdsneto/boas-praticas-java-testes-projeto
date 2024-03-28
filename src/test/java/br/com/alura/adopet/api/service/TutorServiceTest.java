package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository repository;

    @Mock
    private CadastroTutorDto cadastroTutorDto;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Mock
    private Tutor tutor;

    @InjectMocks
    private TutorService tutorService;

    @Test
    @DisplayName("Deve cadastrar o tutor corretamente não existindo cadastro prévio do mesmo email ou telefone")
    void cenario01() {

        //ARRANGE
        when(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).thenReturn(false);

        //ASSERT + ACT
        assertDoesNotThrow(() -> tutorService.cadastrar(cadastroTutorDto));
        then(repository).should().save(new Tutor(cadastroTutorDto));
    }

    @Test
    @DisplayName("Deve impedir o cadastro do tutor por já existir cadastro de telefone ou email")
    void cenario02() {

        //ARRANGE
        when(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).thenReturn(true);

        //ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(cadastroTutorDto));
    }

    @Test
    @DisplayName("Deve atualizar corretamente o tutor")
    void cenario03() {

        //ARRANGE
        given(repository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

        //ACT
        tutorService.atualizar(atualizacaoTutorDto);

        //ASSERT
        then(tutor).should().atualizarDados(atualizacaoTutorDto);

    }

}