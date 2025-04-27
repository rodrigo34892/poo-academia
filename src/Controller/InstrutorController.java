package Controller;

import model.Instrutor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstrutorController {

    public static Connection conectar() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/final";
            String usuario = "root";
            String senha = "root";
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    public void cadastrarInstrutor(Instrutor i) throws SQLException {
        String sql = "INSERT INTO instrutor (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCpf());
            ps.setString(3, i.getEmail());
            ps.setString(4, i.getSenha());
            ps.executeUpdate();
        }
    }

    public List<Instrutor> listarInstrutores() throws SQLException {
        String sql = "SELECT * FROM instrutor";
        List<Instrutor> instrutores = new ArrayList<>();

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Instrutor instrutor = new Instrutor();
                instrutor.setId(rs.getInt("id"));
                instrutor.setNome(rs.getString("nome"));
                instrutor.setCpf(rs.getString("cpf"));
                instrutor.setEmail(rs.getString("email"));
                instrutor.setSenha(rs.getString("senha"));
                instrutores.add(instrutor);
            }
        }
        return instrutores;
    }

    public Instrutor buscarInstrutorPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM instrutor WHERE cpf = ?";
        Instrutor instrutor = null;

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    instrutor = new Instrutor();
                    instrutor.setId(rs.getInt("id"));
                    instrutor.setNome(rs.getString("nome"));
                    instrutor.setCpf(rs.getString("cpf"));
                    instrutor.setEmail(rs.getString("email"));
                    instrutor.setSenha(rs.getString("senha"));
                }
            }
        }
        return instrutor;
    }

    public boolean alterarInstrutor(Instrutor instrutor) throws SQLException {
        String sql = "UPDATE instrutor SET nome = ?, email = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, instrutor.getNome());
            ps.setString(2, instrutor.getEmail());
            ps.setString(3, instrutor.getSenha());
            ps.setString(4, instrutor.getCpf());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletarInstrutor(String cpf) throws SQLException {
        String sql = "DELETE FROM instrutor WHERE cpf = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean validarLogin(String cpf, String senha) {
        String sql = "SELECT senha FROM instrutor WHERE cpf = ?";
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String senhaDB = rs.getString("senha");
                    return senha.equals(senhaDB);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao validar login: " + e.getMessage());
        }
        return false;
    }
}