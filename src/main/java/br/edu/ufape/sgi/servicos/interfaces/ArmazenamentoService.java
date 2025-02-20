package br.edu.ufape.sgi.servicos.interfaces;

import br.edu.ufape.sgi.models.Documento;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArmazenamentoService {
    @Transactional
    List<Documento> salvarArquivo(MultipartFile[] arquivos);
}
