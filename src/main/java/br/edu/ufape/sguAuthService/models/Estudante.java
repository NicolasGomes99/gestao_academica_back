//package br.edu.ufape.sguAuthService.models;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import java.math.BigDecimal;
//
//@Entity
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor
//
//public class Estudante{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private BigDecimal rendaPercapta;
//    private String contatoFamilia;
//    private boolean deficiente;
//    private String tipoDeficiencia;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "dados_bancarios_id", referencedColumnName = "id")
//    private DadosBancarios dadosBancarios;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
//    private Endereco endereco;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "tipo_etnia_id", referencedColumnName = "id")
//    private TipoEtnia tipoEtnia;
//}
