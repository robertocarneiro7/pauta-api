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
@Table(name = "tb_associado")
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long associadoId;
    @Column(nullable = false)
    private String cpf;
    @CreationTimestamp
    private LocalDateTime dataCriacao;
}
