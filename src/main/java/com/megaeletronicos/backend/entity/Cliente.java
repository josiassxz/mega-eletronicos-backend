
package com.megaeletronicos.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    
    @NotBlank(message = "RG é obrigatório")
    @Column(nullable = false, length = 20)
    private String rg;
    
    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 15)
    private String telefone;
    
    @NotBlank(message = "CEP é obrigatório")
    @Column(nullable = false, length = 8)
    private String cep;
    
    @NotBlank(message = "Rua é obrigatória")
    @Column(nullable = false)
    private String rua;
    
    @NotBlank(message = "Número é obrigatório")
    @Column(nullable = false, length = 10)
    private String numero;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Column(nullable = false)
    private String bairro;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Column(nullable = false)
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    @Column(nullable = false, length = 2)
    private String estado;
    
    @NotBlank(message = "Nome da mãe é obrigatório")
    @Column(nullable = false)
    private String nomeMae;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataNascimento;
    
    @NotBlank(message = "Sexo é obrigatório")
    @Column(nullable = false, length = 10)
    private String sexo;
    
    @NotBlank(message = "Estado civil é obrigatório")
    @Column(nullable = false)
    private String estadoCivil;
    
    @NotBlank(message = "Natureza da ocupação é obrigatória")
    @Column(nullable = false)
    private String naturezaOcupacao;
    
    @NotBlank(message = "Profissão é obrigatória")
    @Column(nullable = false)
    private String profissao;
    
    @Column
    private String nomeEmpresa;
    
    @NotNull(message = "Renda mensal é obrigatória")
    @Column(nullable = false)
    private Double rendaMensal;
    
    @Column
    private String fotoDocumento;
    
    @Column
    private String fotoSelfie;

    // Construtores
    public Cliente() {}

    public Cliente(String nome, String email, String cpf, String rg, String telefone, 
                   String cep, String rua, String numero, String bairro, String cidade, 
                   String estado, String nomeMae, LocalDate dataNascimento, String sexo, 
                   String estadoCivil, String naturezaOcupacao, String profissao, 
                   String nomeEmpresa, Double rendaMensal) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.rg = rg;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.nomeMae = nomeMae;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil;
        this.naturezaOcupacao = naturezaOcupacao;
        this.profissao = profissao;
        this.nomeEmpresa = nomeEmpresa;
        this.rendaMensal = rendaMensal;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getNaturezaOcupacao() { return naturezaOcupacao; }
    public void setNaturezaOcupacao(String naturezaOcupacao) { this.naturezaOcupacao = naturezaOcupacao; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public Double getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(Double rendaMensal) { this.rendaMensal = rendaMensal; }

    public String getFotoDocumento() { return fotoDocumento; }
    public void setFotoDocumento(String fotoDocumento) { this.fotoDocumento = fotoDocumento; }

    public String getFotoSelfie() { return fotoSelfie; }
    public void setFotoSelfie(String fotoSelfie) { this.fotoSelfie = fotoSelfie; }
}
