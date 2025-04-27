/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.CadastroExercicio;

public class CadastroExercicioController {

    public boolean inserirExercicio(CadastroExercicio ex) {
        String sql = "INSERT INTO CadastroExercicio (nome, funcao, TipodeExercicio, equipamento, identificacao) "
                + "VALUES (?, ?, ?, ?, ?)";

        Conexao g = new Conexao();
        PreparedStatement comando = null;

        try {
            comando = g.prepararComando(sql);
            comando.setString(1, ex.getNome());
            comando.setString(2, ex.getFuncao());
            comando.setString(3, ex.getTipoDeExercicio());
            comando.setString(4, ex.getEquipamento());
            comando.setString(5, ex.getIdentificacao());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir exercício: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            g.fecharConexao(comando);
        }
    }

    public boolean alterarExercicio(CadastroExercicio ex) {
        String sql = "UPDATE CadastroExercicio SET nome = ?, funcao = ?, TipodeExercicio = ?, equipamento = ?, identificacao = ? WHERE id = ?";

        Conexao g = new Conexao();
        PreparedStatement comando = null;
        boolean sucesso = false;

        try {
            comando = g.prepararComando(sql);

            // Log para diagnóstico
            System.out.println("[DEBUG] Atualizando exercício:");
            System.out.println("ID: " + ex.getId());
            System.out.println("Nome: " + ex.getNome());
            System.out.println("Identificação: " + ex.getIdentificacao());

            comando.setString(1, ex.getNome());
            comando.setString(2, ex.getFuncao());
            comando.setString(3, ex.getTipoDeExercicio());
            comando.setString(4, ex.getEquipamento());
            comando.setString(5, ex.getIdentificacao());
            comando.setInt(6, ex.getId());

            int linhasAfetadas = comando.executeUpdate();
            sucesso = linhasAfetadas > 0;

            System.out.println("[DEBUG] Linhas afetadas: " + linhasAfetadas);

            if (!sucesso) {
                System.err.println("[ERRO] Nenhum registro atualizado. Verifique se o ID existe.");
            }

        } catch (SQLException e) {
            System.err.println("[ERRO SQL] Falha ao atualizar:");
            System.err.println("Código: " + e.getErrorCode());
            System.err.println("Mensagem: " + e.getMessage());

            JOptionPane.showMessageDialog(null,
                    "Erro ao atualizar exercício:\n" + e.getMessage(),
                    "Erro no Banco de Dados",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            g.fecharConexao(comando);
        }

        return sucesso;
    }

    public boolean deletarExercicio(int id) {
        String sql = "DELETE FROM CadastroExercicio WHERE id = ?";

        Conexao g = new Conexao();
        PreparedStatement comando = null;

        try {
            comando = g.prepararComando(sql);
            comando.setInt(1, id);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar exercício: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            g.fecharConexao(comando);
        }
    }

    public CadastroExercicio buscarPorId(int id) {
        String sql = "SELECT * FROM CadastroExercicio WHERE id = ?";
        Conexao g = new Conexao();
        PreparedStatement comando = null;
        ResultSet resultado = null;

        CadastroExercicio ex = new CadastroExercicio();

        try {
            comando = g.prepararComando(sql);
            comando.setInt(1, id);
            resultado = comando.executeQuery();

            if (resultado.next()) {
                ex.setId(resultado.getInt("id"));
                ex.setNome(resultado.getString("nome"));
                ex.setFuncao(resultado.getString("funcao"));
                ex.setTipoDeExercicio(resultado.getString("TipodeExercicio"));
                ex.setEquipamento(resultado.getString("equipamento"));
                ex.setIdentificacao(resultado.getString("identificacao"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar exercício: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            g.fecharConexao(comando, resultado);
        }

        return ex;
    }

    public List<CadastroExercicio> consultar() {
        String sql = "SELECT * FROM CadastroExercicio";
        Conexao g = new Conexao();
        PreparedStatement comando = null;
        ResultSet resultado = null;

        List<CadastroExercicio> lista = new ArrayList<>();

        try {
            comando = g.prepararComando(sql);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                CadastroExercicio ex = new CadastroExercicio();

                ex.setId(resultado.getInt("id"));
                ex.setNome(resultado.getString("nome"));
                ex.setFuncao(resultado.getString("funcao"));
                ex.setTipoDeExercicio(resultado.getString("TipodeExercicio"));
                ex.setEquipamento(resultado.getString("equipamento"));
                ex.setIdentificacao(resultado.getString("identificacao"));

                lista.add(ex);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar exercícios: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            g.fecharConexao(comando, resultado);
        }

        return lista;
    }

    public CadastroExercicio buscarPorIdentificacao(String identificacao) {
        String sql = "SELECT * FROM CadastroExercicio WHERE identificacao = ?";
        Conexao g = new Conexao();
        PreparedStatement comando = null;
        ResultSet resultado = null;

        CadastroExercicio ex = null;

        try {
            comando = g.prepararComando(sql);
            comando.setString(1, identificacao);
            resultado = comando.executeQuery();

            if (resultado.next()) {
                ex = new CadastroExercicio();
                ex.setId(resultado.getInt("id"));
                ex.setNome(resultado.getString("nome"));
                ex.setFuncao(resultado.getString("funcao"));
                ex.setTipoDeExercicio(resultado.getString("TipodeExercicio"));
                ex.setEquipamento(resultado.getString("equipamento"));
                ex.setIdentificacao(resultado.getString("identificacao"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar por identificação: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        } finally {
            g.fecharConexao(comando, resultado);
        }

        return ex;
    }
}
