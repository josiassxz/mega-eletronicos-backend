
package com.megaeletronicos.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    public String salvarArquivo(MultipartFile arquivo) throws IOException {
        if (arquivo.isEmpty()) {
            throw new RuntimeException("Arquivo está vazio");
        }
        
        // Criar diretório se não existir
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Gerar nome único para o arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = "";
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }
        
        String nomeArquivo = UUID.randomUUID().toString() + extensao;
        Path caminhoArquivo = uploadPath.resolve(nomeArquivo);
        
        // Salvar arquivo
        Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
        
        return nomeArquivo;
    }
    
    public void deletarArquivo(String nomeArquivo) throws IOException {
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            Path caminhoArquivo = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminhoArquivo);
        }
    }
    
    public Path obterCaminhoArquivo(String nomeArquivo) {
        return Paths.get(uploadDir).resolve(nomeArquivo);
    }
}
