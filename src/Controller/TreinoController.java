package Controller;

import model.Treino;
import model.Aluno;
import model.Instrutor;
import model.CadastroExercicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class TreinoController {

    private Conexao conexao;

    public TreinoController() {
        conexao = Conexao.getInstance();
    }

    public boolean cadastrarTreino(Treino treino) {
        String sqlTreino = "INSERT INTO treino (nome, id_aluno, id_instrutor, observacoes) VALUES (?, ?, ?, ?)";
        String sqlTreinoExercicio = "INSERT INTO treino_exercicio (id_treino, id_exercicio) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement psTreino = null;
        PreparedStatement psTreinoExercicio = null;
        ResultSet generatedKeys = null;

        try {
            conn = conexao.getConnection();
            conn.setAutoCommit(false);

            // Cadastra o treino
            psTreino = conn.prepareStatement(sqlTreino, Statement.RETURN_GENERATED_KEYS);
            psTreino.setString(1, treino.getNome());
            psTreino.setInt(2, treino.getAluno().getId());
            psTreino.setInt(3, treino.getInstrutor().getId());
            psTreino.setString(4, treino.getObservacoes());

            int affectedRows = psTreino.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar treino, nenhuma linha afetada.");
            }

            // Obtém o ID gerado
            generatedKeys = psTreino.getGeneratedKeys();
            int idTreino;
            if (generatedKeys.next()) {
                idTreino = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao criar treino, nenhum ID obtido.");
            }

            // Cadastra os exercícios do treino
            psTreinoExercicio = conn.prepareStatement(sqlTreinoExercicio);
            for (CadastroExercicio exercicio : treino.getExercicios()) {
                psTreinoExercicio.setInt(1, idTreino);
                psTreinoExercicio.setInt(2, exercicio.getId());
                psTreinoExercicio.addBatch();
            }

            psTreinoExercicio.executeBatch();
            conn.commit();

            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao reverter transação: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar treino: " + e.getMessage());
            return false;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (psTreino != null) {
                    psTreino.close();
                }
                if (psTreinoExercicio != null) {
                    psTreinoExercicio.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public List<Treino> listarTreinos() {
        String sql = "SELECT t.*, a.nome as nome_aluno, a.cpf as cpf_aluno, i.nome as nome_instrutor "
                + "FROM treino t "
                + "JOIN aluno a ON t.id_aluno = a.id "
                + "JOIN instrutor i ON t.id_instrutor = i.id";

        List<Treino> treinos = new ArrayList<>();

        try (Connection conn = conexao.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Treino treino = new Treino();
                treino.setId(rs.getInt("id"));
                treino.setNome(rs.getString("nome"));

                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id_aluno"));
                aluno.setNome(rs.getString("nome_aluno"));
                aluno.setCpf(rs.getString("cpf_aluno"));
                treino.setAluno(aluno);

                Instrutor instrutor = new Instrutor();
                instrutor.setId(rs.getInt("id_instrutor"));
                instrutor.setNome(rs.getString("nome_instrutor"));
                treino.setInstrutor(instrutor);

                treino.setObservacoes(rs.getString("observacoes"));

                // Carrega os exercícios do treino
                List<CadastroExercicio> exercicios = carregarExerciciosTreino(treino.getId(), conn);
                treino.setExercicios(exercicios);

                treinos.add(treino);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar treinos: " + e.getMessage());
        }

        return treinos;
    }

    public List<Treino> buscarTreinosPorAluno(String nomeAluno) {
        String sql = "SELECT t.*, a.nome as nome_aluno, a.cpf as cpf_aluno, i.nome as nome_instrutor "
                + "FROM treino t "
                + "JOIN aluno a ON t.id_aluno = a.id "
                + "JOIN instrutor i ON t.id_instrutor = i.id "
                + "WHERE a.nome LIKE ?";

        List<Treino> treinos = new ArrayList<>();

        try (Connection conn = conexao.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nomeAluno + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Treino treino = new Treino();
                    treino.setId(rs.getInt("id"));
                    treino.setNome(rs.getString("nome"));

                    Aluno aluno = new Aluno();
                    aluno.setId(rs.getInt("id_aluno"));
                    aluno.setNome(rs.getString("nome_aluno"));
                    aluno.setCpf(rs.getString("cpf_aluno"));
                    treino.setAluno(aluno);

                    Instrutor instrutor = new Instrutor();
                    instrutor.setId(rs.getInt("id_instrutor"));
                    instrutor.setNome(rs.getString("nome_instrutor"));
                    treino.setInstrutor(instrutor);

                    treino.setObservacoes(rs.getString("observacoes"));

                    // Carrega os exercícios do treino
                    List<CadastroExercicio> exercicios = carregarExerciciosTreino(treino.getId(), conn);
                    treino.setExercicios(exercicios);

                    treinos.add(treino);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar treinos por aluno: " + e.getMessage());
        }

        return treinos;
    }

    private List<CadastroExercicio> carregarExerciciosTreino(int idTreino, Connection conn) throws SQLException {
        String sql = "SELECT e.* FROM exercicio e "
                + "JOIN treino_exercicio te ON e.id = te.id_exercicio "
                + "WHERE te.id_treino = ?";

        List<CadastroExercicio> exercicios = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTreino);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CadastroExercicio exercicio = new CadastroExercicio();
                    exercicio.setId(rs.getInt("id"));
                    exercicio.setNome(rs.getString("nome"));
                    exercicio.setFuncao(rs.getString("funcao"));
                    exercicio.setTipoDeExercicio(rs.getString("tipo_exercicio"));
                    exercicio.setEquipamento(rs.getString("equipamento"));
                    exercicio.setIdentificacao(rs.getString("identificacao"));

                    exercicios.add(exercicio);
                }
            }
        }

        return exercicios;
    }
}
