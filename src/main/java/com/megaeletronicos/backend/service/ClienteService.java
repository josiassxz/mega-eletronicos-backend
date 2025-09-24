
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }
    
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Cliente> buscarPorCidade(String cidade) {
        return clienteRepository.findByCidade(cidade);
    }
    
    public List<Cliente> buscarPorEstado(String estado) {
        return clienteRepository.findByEstado(estado);
    }
    
    public Cliente salvar(Cliente cliente) {
        // Verificar se já existe cliente com mesmo email ou CPF
        if (cliente.getId() == null) {
            if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
                throw new RuntimeException("Já existe um cliente com este email");
            }
            if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
                throw new RuntimeException("Já existe um cliente com este CPF");
            }
        } else {
            // Para atualizações, verificar se email/CPF não pertencem a outro cliente
            Optional<Cliente> clienteComMesmoEmail = clienteRepository.findByEmail(cliente.getEmail());
            if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Já existe outro cliente com este email");
            }
            
            Optional<Cliente> clienteComMesmoCpf = clienteRepository.findByCpf(cliente.getCpf());
            if (clienteComMesmoCpf.isPresent() && !clienteComMesmoCpf.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Já existe outro cliente com este CPF");
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        clienteAtualizado.setId(id);
        return salvar(clienteAtualizado);
    }
}
