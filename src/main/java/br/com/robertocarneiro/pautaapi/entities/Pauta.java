package br.com.robertocarneiro.pautaapi.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pautaId;
    private String nome;
    private String descricao;
    private LocalDateTime dataAberturaVotacao;
    private LocalDateTime dataEncerramentoVotacao;
    @CreationTimestamp
    private LocalDateTime dataCriacao;
}
