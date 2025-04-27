package model;

import java.util.List;

public class Treino {
    private int id;
    private String nome;
    private Aluno aluno;
    private Instrutor instrutor;
    private List<CadastroExercicio> exercicios;
    private String observacoes;

    public Treino() {
    }

    public Treino(String nome, Aluno aluno, Instrutor instrutor, List<CadastroExercicio> exercicios, String observacoes) {
        this.nome = nome;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.exercicios = exercicios;
        this.observacoes = observacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }

    public List<CadastroExercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<CadastroExercicio> exercicios) {
        this.exercicios = exercicios;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}