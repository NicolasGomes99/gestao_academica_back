package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.dados.CursoRepository;
import br.edu.ufape.sguAuthService.exceptions.ExceptionUtil;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.models.Curso;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class CursoService implements br.edu.ufape.sguAuthService.servicos.interfaces.CursoService {
    private final CursoRepository cursoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Curso salvar(Curso curso) {
        try {
            if(cursoRepository.existsByNome(curso.getNome())) {
                Curso cursoDesativado = cursoRepository.findByNomeAndAtivoFalse(curso.getNome());
                if (cursoDesativado != null) {
                    cursoDesativado.setAtivo(true);
                    return cursoRepository.save(cursoDesativado);
                }
            }
            return cursoRepository.save(curso);
        }catch (DataIntegrityViolationException e){
          throw  ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public Curso buscar(Long id) throws CursoNotFoundException {
        return cursoRepository.findById(id).orElseThrow(CursoNotFoundException::new);
    }

    @Override
    public List<Curso> listar() {
        return cursoRepository.findByAtivoTrue();
    }

    @Override
    public List<Usuario> listarAlunosPorCurso(Long id){
        return cursoRepository.findAllAlunosByCursoId(id);
    }

    @Override
    public Curso editar(Long id, Curso novoCurso) throws CursoNotFoundException{
        try {
            Curso antigoCurso = cursoRepository.findById(id).orElseThrow(CursoNotFoundException::new);
            modelMapper.map(novoCurso, antigoCurso);
            return cursoRepository.save(antigoCurso);
        } catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletar(Long id) throws CursoNotFoundException {
        Curso curso = cursoRepository.findById(id).orElseThrow(CursoNotFoundException::new);
        curso.setAtivo(false);
        cursoRepository.save(curso);
    }


}
