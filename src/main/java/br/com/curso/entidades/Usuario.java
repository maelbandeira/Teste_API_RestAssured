package br.com.curso.entidades;

public class Usuario {

    private String nome;
    private Integer ano;
    private Double salario;

    public Usuario(String nome, Integer ano) {
        this.nome = nome;
        this.ano = ano;
    }

    private Usuario(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }


}
