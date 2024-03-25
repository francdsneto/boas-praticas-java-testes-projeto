package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Mock
    private Adocao adocao1;
    @Mock
    private Adocao adocao2;
    @Mock
    private Adocao adocao3;
    @Mock
    private Adocao adocao4;
    @Mock
    private Adocao adocao5;

    @InjectMocks
    ValidacaoTutorComLimiteDeAdocoes validacaoTutorComLimiteDeAdocoes;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Test
    @DisplayName("Deve impedir adocao pois tutor já contém 5 adocoes aprovadas")
    void cenario1() {

        //ARRANGE
        when(tutorRepository.getReferenceById(dto.idTutor())).thenReturn(tutor);
        configurarAdocoesArray(adocao1);
        configurarAdocoesArray(adocao2);
        configurarAdocoesArray(adocao3);
        configurarAdocoesArray(adocao4);
        configurarAdocoesArray(adocao5);
        when(adocaoRepository.findAll()).thenReturn(adocoes);

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> validacaoTutorComLimiteDeAdocoes.validar(dto));

    }

    @Test
    @DisplayName("Deve permitir adocao pois tutor contém menos que 5 adocoes aprovadas")
    void cenario2() {

        //ARRANGE
        when(tutorRepository.getReferenceById(dto.idTutor())).thenReturn(tutor);
        configurarAdocoesArray(adocao1);
        configurarAdocoesArray(adocao2);
        configurarAdocoesArray(adocao3);
        configurarAdocoesArray(adocao4);
        when(adocaoRepository.findAll()).thenReturn(adocoes);

        //ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacaoTutorComLimiteDeAdocoes.validar(dto));

    }

    private void configurarAdocoesArray(Adocao adocao) {
        when(adocao.getStatus()).thenReturn(StatusAdocao.APROVADO);
        when(adocao.getTutor()).thenReturn(tutor);
        adocoes.add(adocao);
    }

}