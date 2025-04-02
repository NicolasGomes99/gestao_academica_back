//package br.edu.ufape.sguAuthService.comunicacao.dto.endereco;
//
//import br.edu.ufape.sguAuthService.models.Endereco;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.modelmapper.ModelMapper;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class EnderecoRequest {
//    private Long id;
//    private String rua;
//    private String cep;
//    private String cidade;
//    private String estado;
//    private String numero;
//    private String complemento;
//
//    public Endereco convertToEntity(EnderecoRequest enderecoRequest, ModelMapper modelMapper) {
//        return modelMapper.map(enderecoRequest, Endereco.class);
//    }
//}
