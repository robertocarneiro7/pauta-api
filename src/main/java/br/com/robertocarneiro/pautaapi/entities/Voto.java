package br.com.robertocarneiro.pautaapi.entities;

import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
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
@Table(name = "tb_voto")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long votoId;
    @Enumerated(value = EnumType.STRING)
    private EnumBoolean resposta;
    @CreationTimestamp
    private LocalDateTime dataCriacao;
    @ManyToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;
    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;
}
