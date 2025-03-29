package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguAuthService.models.Documento;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArmazenamentoService {
    @Transactional
    List<Documento> salvarArquivo(MultipartFile[] arquivos);

    List<DocumentoResponse> converterDocumentosParaBase64(List<Documento> documentos) throws IOException;
}
