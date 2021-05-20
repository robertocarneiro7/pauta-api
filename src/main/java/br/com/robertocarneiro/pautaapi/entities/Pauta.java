package br.com.robertocarneiro.pautaapi.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pautaId;
    @Column(nullable = false, unique = true)
    private String nome;
    @Column(nullable = false)
    private String descricao;
    private LocalDateTime dataAberturaVotacao;
    private LocalDateTime dataEncerramentoVotacao;
    @CreationTimestamp
    private LocalDateTime dataCriacao;
}
