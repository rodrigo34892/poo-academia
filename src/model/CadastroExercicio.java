/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pichau
 */
public class CadastroExercicio {

    private int id;
    private String nome;
    private String funcao;
    private String tipoDeExercicio;
    private String equipamento;
    private String identificacao;

    //construtor vazio
    public CadastroExercicio() {
    }

    //para inserções
    public CadastroExercicio(String nome, String funcao, String tipoDeExercicio,
            String equipamento, String identificacao) {
        this.nome = nome;
        this.funcao = funcao;
        this.tipoDeExercicio = tipoDeExercicio;
        this.equipamento = equipamento;
        this.identificacao = identificacao;
    }

    //para atualizações
    public CadastroExercicio(int id, String nome, String funcao, String tipoDeExercicio,
            String equipamento, String identificacao) {
        this.id = id;
        this.nome = nome;
        this.funcao = funcao;
        this.tipoDeExercicio = tipoDeExercicio;
        this.equipamento = equipamento;
        this.identificacao = identificacao;
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

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getTipoDeExercicio() {
        return tipoDeExercicio;
    }

    public void setTipoDeExercicio(String tipoDeExercicio) {
        this.tipoDeExercicio = tipoDeExercicio;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    // método toString para representação textual do objeto
    @Override
    public String toString() {
        return "CadastroExercicio{"
                + "id=" + id
                + ", nome=" + nome
                + ", funcao=" + funcao
                + ", tipoDeExercicio=" + tipoDeExercicio
                + ", equipamento=" + equipamento
                + ", identificacao=" + identificacao + '}';
    }

}
