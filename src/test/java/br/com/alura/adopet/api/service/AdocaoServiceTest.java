package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @Mock
    private AdocaoRepository repository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private EmailService emailService;
    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;
    @Mock
    private Tutor tutor;
    @Mock
    private Abrigo abrigo;
    @Spy
    private Adocao adocao;

    private SolicitacaoAdocaoDto dto;
    @InjectMocks
    AdocaoService adocaoService;
    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Mock
    private AprovacaoAdocaoDto aprovacaoDTO;

    @Mock
    private ReprovacaoAdocaoDto reprovacaoDTO;

    @Mock
    private LocalDateTime data;

    @Test
    void deveriaSalvarAdocaoAoSolicitar() {

        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(1l, 1l, "motivo");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        adocaoService.solicitar(dto);

        //ASSERT
        then(repository).should().save(adocaoCaptor.capture());

        Adocao adocaoSalva = adocaoCaptor.getValue();

        Assertions.assertThat(adocaoSalva.getPet()).isEqualTo(pet);
        Assertions.assertThat(adocaoSalva.getTutor()).isEqualTo(tutor);
        Assertions.assertThat(adocaoSalva.getMotivo()).isEqualTo(dto.motivo());

    }

    @Test
    void deveriaChamarValidadoresDeAdocaoAoSolicitar() {

        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(1l, 1l, "motivo");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        adocaoService.solicitar(dto);

        //ASSERT
        BDDMockito.then(validador1).should().validar(dto);
        BDDMockito.then(validador2).should().validar(dto);

    }

    @Test
    @DisplayName("Deverá aprovar a adoção e enviar o e-mail")
    void aprovarAdocao() {

        //ARRANGE
        when(repository.getReferenceById(aprovacaoDTO.idAdocao())).thenReturn(adocao);
        when(adocao.getPet()).thenReturn(pet);
        when(adocao.getPet().getAbrigo()).thenReturn(abrigo);
        when(adocao.getTutor()).thenReturn(tutor);
        when(adocao.getData()).thenReturn(data);

        //ACT
        adocaoService.aprovar(aprovacaoDTO);

        //ASSERT
        Assertions.assertThat(adocao.getStatus()).isEqualTo(StatusAdocao.APROVADO);
        then(emailService).should().enviarEmail(any(), any(), any());
    }

    @Test
    @DisplayName("Deverá reprovar a adoção e enviar o e-mail")
    void reprovarAdocao() {

        //ARRANGE
        when(repository.getReferenceById(reprovacaoDTO.idAdocao())).thenReturn(adocao);
        when(adocao.getPet()).thenReturn(pet);
        when(adocao.getPet().getAbrigo()).thenReturn(abrigo);
        when(adocao.getTutor()).thenReturn(tutor);
        when(adocao.getData()).thenReturn(data);

        //ACT
        adocaoService.reprovar(reprovacaoDTO);

        //ASSERT
        Assertions.assertThat(adocao.getStatus()).isEqualTo(StatusAdocao.REPROVADO);
        Assertions.assertThat(adocao.getJustificativaStatus()).isEqualTo(reprovacaoDTO.justificativa());
        then(emailService).should().enviarEmail(any(), any(), any());
    }

}