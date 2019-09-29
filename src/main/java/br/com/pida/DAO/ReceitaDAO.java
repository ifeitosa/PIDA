package br.com.pida.DAO;

import br.com.pida.conexao.ConectarBD;
import br.com.pida.modelo.ItemReceita;
import br.com.pida.modelo.Receita;
import br.com.pida.util.DateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author italo
 */
public class ReceitaDAO {
    
    private String sql;
    private Connection con;
    private PreparedStatement ps;
    
    public List<String> pesquisarReceitas() {
        con = ConectarBD.abrirConexao();
        sql = "SELECT Nome FROM Receita";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<String> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(rs.getString("Nome"));
            }
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
    
    public List<String> pesquisarReceitas(String parte) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT Nome FROM Receita WHERE LOWER(Nome) LIKE ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + parte.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            List<String> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(rs.getString("Nome"));
            }
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
    
    public boolean inserirReceita(Receita receita) {
        con = ConectarBD.abrirConexao();
        
        sql = "INSERT INTO Receita (Nome, DataRegistro) VALUES (?, ?)";
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, receita.getNome());
            ps.setDate(2, DateUtil.toDatabase(receita.getRegistro()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                receita.setId(rs.getInt(1));
                sql = "SELECT DataRegistro FROM Receita WHERE IdReceita = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, receita.getId());
                rs = ps.executeQuery();
                if (rs.next()) {
                    receita.setRegistro(DateUtil.fromDatabase(rs.getDate(1)));
                }
            }
            sql = "INSERT INTO ProdutoReceita (Receita, Produto, Quantidade, Unidade) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            for(ItemReceita ir : receita.getItens()) {
                ps.setInt(1, receita.getId());
                ps.setInt(2, ir.getProdutoId());
                ps.setDouble(3, ir.getQuantidade());
                ps.setString(4, ir.getUnidade());
                ps.execute();
            }
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
    }
    
    public Receita obterReceita(String nome) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT IdReceita, Nome, DataRegistro FROM Receita WHERE LOWER(Nome) LIKE ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Receita resultado = new Receita();
                resultado.setId(rs.getInt("IdReceita"));
                resultado.setNome(rs.getString("Nome"));
                resultado.setRegistro(DateUtil.fromDatabase(rs.getDate("DataRegistro")));
                resultado.setItens(obterItens(resultado.getId()));
                return resultado;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<ItemReceita> obterItens(int idReceita) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT PR.Produto as IdProduto, P.Produto as Descricao, PR.Quantidade AS QuantidadeReceita, Unidade FROM ProdutoReceita as PR LEFT JOIN Produto as P ON PR.Produto = P.IdProduto WHERE Receita = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReceita);
            List<ItemReceita> itens = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            
            
            while (rs.next()) {
                ItemReceita ir = new ItemReceita();
                System.out.println(rs.getInt("IdProduto"));
                ir.setProdutoId(rs.getInt("IdProduto"));
                ir.setProduto(rs.getString("Descricao"));
                System.out.println(rs.getDouble("QuantidadeReceita"));
                ir.setQuantidade(rs.getDouble("QuantidadeReceita"));
                ir.setUnidade(rs.getString("Unidade"));
                itens.add(ir);
            }
            return itens;
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
    
    public void deletarItemReceita(Receita receita, ItemReceita item) {
        con = ConectarBD.abrirConexao();
        sql = "DELETE FROM ProdutoReceita WHERE Receita = ? and Produto = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, receita.getId());
            ps.setInt(2, item.getProdutoId());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void atualizarItemReceita(Receita receita, ItemReceita novo) {
        con = ConectarBD.abrirConexao();
        sql = "UPDATE ProdutoReceita SET Quantidade = ?, Unidade = ? WHERE Receita = ? AND Produto = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, novo.getQuantidade());
            ps.setString(2, novo.getUnidade());
            ps.setInt(3, receita.getId());
            ps.setInt(4, novo.getProdutoId());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void adicionarItemReceita(Receita receita, ItemReceita item) {
        con = ConectarBD.abrirConexao();
        sql = "INSERT INTO ProdutoReceita (Receita, Produto, Quantidade, Unidade) VALUES (?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, receita.getId());
            ps.setInt(2, item.getProdutoId());
            ps.setDouble(3, item.getQuantidade());
            ps.setString(4, item.getUnidade());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ReceitaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
