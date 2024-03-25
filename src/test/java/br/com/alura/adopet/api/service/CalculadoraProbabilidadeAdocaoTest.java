package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    @DisplayName("Deve devolver probabilidade ALTA para idade baixa e peso baixo")
    void probabilidadeAltaCenario1() {
        //idade 4 anos e 4kg - ALTA

        //ARRANGE
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo feliz","85999999999", "abrigofeliz@gmail.com"));

        Pet pet = new Pet(new CadastroPetDto(TipoPet.GATO,"Miau","Siames",4,"Cinza",4.0f), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        //ACT
        var probabilidade = calculadora.calcular(pet);

        //ASSERT
        Assertions.assertThat(probabilidade).isEqualByComparingTo(ProbabilidadeAdocao.ALTA);
    }

    @Test
    @DisplayName("Deve devolver probabilidade MEDIA para idade ALTA e peso baixo")
    void probabilidadeMediaCenario1() {
        //idade 15 anos e 4kg - MEDIA

        //ARRANGE
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo feliz","85999999999", "abrigofeliz@gmail.com"));

        Pet pet = new Pet(new CadastroPetDto(TipoPet.GATO,"Miau","Siames",15,"Cinza",4.0f), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        //ACT
        var probabilidade = calculadora.calcular(pet);

        //ASSERT
        Assertions.assertThat(probabilidade).isEqualByComparingTo(ProbabilidadeAdocao.MEDIA);
    }

}