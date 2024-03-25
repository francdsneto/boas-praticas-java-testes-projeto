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
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Mock
    private Adocao adocao;

    @InjectMocks
    ValidacaoTutorComAdocaoEmAndamento validacaoTutorComAdocaoEmAndamento;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Test
    @DisplayName("Deve impedir a adoção pelo tutor pois este possui adoção em andamento")
    void deveImpedirAdocaoSeTutorTemAdocaoEmAndamento() {

        //ARRANGE
        when(tutorRepository.getReferenceById(dto.idTutor())).thenReturn(tutor);
        when(adocao.getStatus()).thenReturn(StatusAdocao.AGUARDANDO_AVALIACAO);
        when(adocao.getTutor()).thenReturn(tutor);
        adocoes.add(adocao);
        when(adocaoRepository.findAll()).thenReturn(adocoes);

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> validacaoTutorComAdocaoEmAndamento.validar(dto));

    }

    @Test
    @DisplayName("Deve permitir a adoção pelo tutor")
    void devePermitirAdocaoDoTutor() {

        //ARRANGE
        when(tutorRepository.getReferenceById(dto.idTutor())).thenReturn(tutor);
        when(adocao.getStatus()).thenReturn(StatusAdocao.APROVADO);
        when(adocao.getTutor()).thenReturn(tutor);
        adocoes.add(adocao);
        when(adocaoRepository.findAll()).thenReturn(adocoes);

        //ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacaoTutorComAdocaoEmAndamento.validar(dto));

    }


}