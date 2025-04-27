package Controller;

import model.Aluno;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CadastroAluno;

public class CadastroAlunoController {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/final";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void cadastrarAluno(CadastroAluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getCpf());
            ps.setString(3, aluno.getEmail());
            ps.setString(4, aluno.getSenha());
            ps.executeUpdate();
        }
    }

    public List<CadastroAluno> listarAlunos() throws SQLException {
        String sql = "SELECT * FROM aluno";
        List<CadastroAluno> alunos = new ArrayList<>();

        try (Connection conn = conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CadastroAluno aluno = new CadastroAluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setEmail(rs.getString("email"));
                aluno.setSenha(rs.getString("senha"));
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public CadastroAluno buscarAlunoPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM aluno WHERE cpf = ?";
        CadastroAluno aluno = null;

        try (Connection conn = conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    aluno = new CadastroAluno();
                    aluno.setId(rs.getInt("id"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setCpf(rs.getString("cpf"));
                    aluno.setEmail(rs.getString("email"));
                    aluno.setSenha(rs.getString("senha"));
                }
            }
        }
        return aluno;
    }

    public boolean atualizarAluno(CadastroAluno aluno) throws SQLException {
        String sql = "UPDATE aluno SET nome = ?, email = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getEmail());
            ps.setString(3, aluno.getSenha());
            ps.setString(4, aluno.getCpf());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletarAluno(String cpf) throws SQLException {
        String sql = "DELETE FROM aluno WHERE cpf = ?";

        try (Connection conn = conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;
        }
    }
}
