package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguAuthService.models.Documento;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArmazenamentoService implements br.edu.ufape.sguAuthService.servicos.interfaces.ArmazenamentoService {
    private final List<String> tiposPermitidos = List.of("application/pdf", "image/jpeg", "image/png", "image/jpg");
    @Value("${arquivo.diretorio-upload}")
    private String uploadDir;

    @Transactional
    @Override
    public List<Documento> salvarArquivo(MultipartFile[] arquivos) {
        List<Documento> documentosSalvos = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            if (!tiposPermitidos.contains(arquivo.getContentType())) {
                throw new IllegalArgumentException("Tipo de arquivo não permitido!");
            }
            String uuid = UUID.randomUUID().toString();
            String extensao = FilenameUtils.getExtension(arquivo.getOriginalFilename());
            String nomeArquivoComUUID = uuid + "." + extensao;

            try {
                // Salva o arquivo no diretório desejado

                Path caminho = Paths.get(uploadDir, nomeArquivoComUUID);
                Files.copy(arquivo.getInputStream(), caminho);

                // Cria uma nova instância de Documento
                Documento documento = new Documento();
                documento.setNome(nomeArquivoComUUID); // Armazena o nome com UUID
                documento.setPath(caminho.toString());
                documentosSalvos.add(documento);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao salvar arquivo!");

            }
        }
        return documentosSalvos;
    }

    @Override
    public List<DocumentoResponse> converterDocumentosParaBase64(List<Documento> documentos) throws IOException {
        List<DocumentoResponse> documentosBase64 = new ArrayList<>();
        for (Documento documento : documentos) {
            Path filePath = Paths.get(uploadDir).resolve(documento.getPath()).normalize();
            if (Files.exists(filePath)) {
                byte[] fileBytes = Files.readAllBytes(filePath);
                String base64 = Base64.getEncoder().encodeToString(fileBytes);
                documentosBase64.add(new DocumentoResponse(documento.getNome(), base64));
            }
        }
        return documentosBase64;
    }
}
