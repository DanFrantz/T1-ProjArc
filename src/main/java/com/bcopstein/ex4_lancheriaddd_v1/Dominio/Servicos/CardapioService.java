package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioCorrenteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class CardapioService {
    private CardapioRepository cardapioRepository;
    private CardapioCorrenteRepository cardapioCorrenteRepository;

    @Autowired
    public CardapioService(CardapioRepository cardapioRepository,
                           CardapioCorrenteRepository cardapioCorrenteRepository){
        this.cardapioRepository = cardapioRepository;
        this.cardapioCorrenteRepository = cardapioCorrenteRepository;
    }

    public Cardapio recuperaCardapio(long Id){
        return cardapioRepository.recuperaPorId(Id);
    }

    public Cardapio recuperaCardapioCorrente(){
        long idCardapioCorrente = cardapioCorrenteRepository.recuperaIdCardapioCorrente();
        return cardapioRepository.recuperaPorId(idCardapioCorrente);
    }

    public List<CabecalhoCardapio> recuperaListaDeCardapios(){
        return cardapioRepository.cardapiosDisponiveis();
    }

    public List<Produto> recuperaSugestoesDoChef(){
        return cardapioRepository.indicacoesDoChef();
    }
}
