
package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.service.ClienteService;
import com.megaeletronicos.backend.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por filtros")
    public ResponseEntity<List<Cliente>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado) {
        
        List<Cliente> clientes;
        
        if (email != null) {
            Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
            clientes = cliente.map(List::of).orElse(List.of());
        } else if (cpf != null) {
            Optional<Cliente> cliente = clienteService.buscarPorCpf(cpf);
            clientes = cliente.map(List::of).orElse(List.of());
        } else if (nome != null) {
            clientes = clienteService.buscarPorNome(nome);
        } else if (cidade != null) {
            clientes = clienteService.buscarPorCidade(cidade);
        } else if (estado != null) {
            clientes = clienteService.buscarPorEstado(estado);
        } else {
            clientes = clienteService.listarTodos();
        }
        
        return ResponseEntity.ok(clientes);
    }
    
    @PostMapping
    @Operation(summary = "Criar novo cliente")
    public ResponseEntity<?> criar(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            // Buscar cliente para deletar fotos associadas
            Optional<Cliente> cliente = clienteService.buscarPorId(id);
            if (cliente.isPresent()) {
                try {
                    if (cliente.get().getFotoDocumento() != null) {
                        fileStorageService.deletarArquivo(cliente.get().getFotoDocumento());
                    }
                    if (cliente.get().getFotoSelfie() != null) {
                        fileStorageService.deletarArquivo(cliente.get().getFotoSelfie());
                    }
                } catch (IOException e) {
                    // Log do erro, mas não impede a exclusão do cliente
                    System.err.println("Erro ao deletar arquivos: " + e.getMessage());
                }
            }
            
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PostMapping("/{id}/foto-documento")
    @Operation(summary = "Upload da foto do documento")
    public ResponseEntity<?> uploadFotoDocumento(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto anterior se existir
            if (cliente.getFotoDocumento() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoDocumento());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto anterior: " + e.getMessage());
                }
            }
            
            // Salvar nova foto
            String nomeArquivo = fileStorageService.salvarArquivo(arquivo);
            cliente.setFotoDocumento(nomeArquivo);
            clienteService.salvar(cliente);
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto do documento enviada com sucesso");
            resposta.put("arquivo", nomeArquivo);
            return ResponseEntity.ok(resposta);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PostMapping("/{id}/foto-selfie")
    @Operation(summary = "Upload da foto selfie")
    public ResponseEntity<?> uploadFotoSelfie(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto anterior se existir
            if (cliente.getFotoSelfie() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoSelfie());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto anterior: " + e.getMessage());
                }
            }
            
            // Salvar nova foto
            String nomeArquivo = fileStorageService.salvarArquivo(arquivo);
            cliente.setFotoSelfie(nomeArquivo);
            clienteService.salvar(cliente);
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto selfie enviada com sucesso");
            resposta.put("arquivo", nomeArquivo);
            return ResponseEntity.ok(resposta);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @GetMapping("/uploads/{nomeArquivo}")
    @Operation(summary = "Visualizar arquivo enviado")
    public ResponseEntity<Resource> visualizarArquivo(@PathVariable String nomeArquivo) {
        try {
            Path caminhoArquivo = fileStorageService.obterCaminhoArquivo(nomeArquivo);
            Resource resource = new UrlResource(caminhoArquivo.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
