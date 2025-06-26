package br.edu.ufape.sguAuthService.comunicacao.dto.aluno;



import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.Curso;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AlunoRequest {

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(min = 1, max = 100, message = "A matrícula deve ter entre 1 e 100 caracteres")
    private String matricula;
    @NotNull(message = "O curso é obrigatório")
    @Positive(message = "O ID do curso deve ser um número positivo")
    private Long cursoId;

    @NotNull(message = "Os documentos são obrigatórios")
    @Size(min = 1, message = "Pelo menos um documento deve ser fornecido")
    private MultipartFile[] documentos;

    public Aluno convertToEntity( ModelMapper modelMapper, Fachada fachada) throws CursoNotFoundException {
        Curso curso = fachada.buscarCurso(this.getCursoId());
        modelMapper.typeMap(AlunoRequest.class, Aluno.class).addMappings(mapper -> {
            mapper.skip(Aluno::setId);  // Ignora o campo ID
        });
        Aluno aluno = modelMapper.map(this, Aluno.class);
        aluno.setCurso(curso);
        return aluno;
    }

}
