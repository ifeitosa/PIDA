/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.dao;

import br.com.pida.conexao.ConectarBD;
import br.com.pida.modelo.Lote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import br.com.pida.modelo.Produto;
import br.com.pida.util.DateUtil;
import java.io.Serializable;
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
public class ProdutoDAO implements Serializable {

    private String sql;
    private Connection con;
    private PreparedStatement ps;

    public boolean inserirProduto(Produto produto) {
        con = ConectarBD.abrirConexao();
        sql = "INSERT INTO Produto (CodigoBarras, Produto, PrecoVenda) VALUES (?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, produto.getCodigoBarras());
            ps.setString(2, produto.getProduto());
            ps.setDouble(3, produto.getPrecoVenda());
            ps.execute();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void atualizarProduto(Produto produto) {
        con = ConectarBD.abrirConexao();
        sql = "UPDATE Produto SET Produto = ?, PrecoVenda = ? WHERE CodigoBarras = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, produto.getProduto());
            ps.setDouble(2, produto.getPrecoVenda());
            ps.setString(3, produto.getCodigoBarras());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> pesquisarNomes(String parte) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT Produto FROM Produto WHERE LOWER(Produto) LIKE ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + parte.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            List<String> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(rs.getString("Produto"));
            }
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    public Produto pesquisarProduto(String codigoBarras) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT IdProduto, CodigoBarras, Produto, PrecoVenda FROM Produto WHERE CodigoBarras = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codigoBarras);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("IdProduto"));
                produto.setCodigoBarras(rs.getString("CodigoBarras"));
                produto.setProduto(rs.getString("Produto"));
                produto.setPrecoVenda(rs.getDouble("PrecoVenda"));

                ArrayList<Lote> lotes = new ArrayList<>();
                sql = "SELECT IdLote, Quantidade, Vencimento FROM Lote WHERE Produto = ? AND Quantidade > 0 ORDER BY Vencimento";
                ps = con.prepareStatement(sql);
                ps.setInt(1, produto.getId());
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    lotes.add(new Lote(rs2.getInt("IdLote"), rs2.getDouble("Quantidade"), DateUtil.fromDatabase(rs2.getDate("Vencimento"))));
                    // System.out.println("Lote encontrado: " + rs2.getInt("IdLote") + " " + rs2.getDouble("Quantidade") + " " + rs2.getDate("Vencimento").toString());
                }
                produto.setLotes(lotes);
                return produto;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
    public int obterProduto(String produto) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT IdProduto, Produto FROM Produto WHERE Produto = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, produto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("IdProduto");
            }
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public boolean inserirLote(Produto produto, Lote lote) {
        con = ConectarBD.abrirConexao();
        sql = "INSERT INTO Lote (Produto, Quantidade, Vencimento) VALUES (?, ?, ?)";
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, produto.getId());
            ps.setDouble(2, lote.getQuantidade());
            ps.setDate(3, DateUtil.toDatabase(lote.getVencimento()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                lote.setId(rs.getInt(1));
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean atualizarLote(Lote lote) {
        con = ConectarBD.abrirConexao();
        sql = "UPDATE Lote SET Quantidade = ?, Vencimento = ? WHERE IdLote = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, lote.getQuantidade());
            ps.setDate(2, DateUtil.toDatabase(lote.getVencimento()));
            ps.setInt(3, lote.getId());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Lote> obterLotes(int produtoId) {
        con = ConectarBD.abrirConexao();
        sql = "SELECT IdLote, Quantidade, Vencimento FROM Lote WHERE Produto = ? AND Quantidade > 0 ORDER BY Vencimento";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, produtoId);
            ResultSet rs = ps.executeQuery();
            List<Lote> lotes = new ArrayList<>();
            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("IdLote"));
                lote.setQuantidade(rs.getDouble("Quantidade"));
                lote.setVencimento(DateUtil.fromDatabase(rs.getDate("Vencimento")));
                lotes.add(lote);
            }
            return lotes;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    public List<Produto> obterVencimentos(java.util.Date start, java.util.Date end) {
        java.sql.Date startDate = DateUtil.toDatabase(start);
        java.sql.Date endDate = DateUtil.toDatabase(end);
        List<Produto> produtos = new ArrayList<>();
        con = ConectarBD.abrirConexao();
        sql = "SELECT IdLote, Quantidade, Vencimento, IdProduto, CodigoBarras, Produto.Produto, Produto.PrecoVenda FROM Lote JOIN Produto ON Lote.Produto = Produto.IdProduto WHERE Quantidade > 0 AND Vencimento BETWEEN ? AND ? ORDER BY IdProduto";
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();
            String codigoBarrasAnterior = "";
            Produto produto = new Produto();
            while (rs.next()) {                
                if (codigoBarrasAnterior.equals(rs.getString("CodigoBarras"))) {
                    Lote lote = new Lote(rs.getInt("IdLote"), rs.getDouble("Quantidade"), DateUtil.fromDatabase(rs.getDate("Vencimento")));
                    produto.getLotes().add(lote);
                } else {
                    produto = new Produto();
                    produto.setCodigoBarras(rs.getString("CodigoBarras"));
                    produto.setId(rs.getInt("IdProduto"));
                    produto.setProduto(rs.getString("Produto.Produto"));
                    produto.setPrecoVenda(rs.getDouble("PrecoVenda"));
                    Lote lote = new Lote(rs.getInt("IdLote"), rs.getDouble("Quantidade"), DateUtil.fromDatabase(rs.getDate("Vencimento")));
                    produto.getLotes().add(lote);
                    produtos.add(produto);
                }
                codigoBarrasAnterior = produto.getCodigoBarras();
            }            
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produtos;
    }
    
    public List<String> obterListaProdutos() {
        List<String> resultado = new ArrayList<>();
        con = ConectarBD.abrirConexao();
        sql = "SELECT Produto FROM Produto";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(rs.getString("Produto"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public Produto produtoPorNome(String produto) {
        con = ConectarBD.abrirConexao();
        sql = sql = "SELECT IdProduto, CodigoBarras, Produto, PrecoVenda FROM Produto WHERE Produto = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, produto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Produto prod = new Produto();
                prod.setCodigoBarras(rs.getString("CodigoBarras"));
                prod.setId(rs.getInt("IdProduto"));
                prod.setProduto(rs.getString("Produto"));
                prod.setPrecoVenda(rs.getDouble("PrecoVenda"));
                return prod;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
    public List<String> obterCodigosBarras(String parte) {
        List<String> codigos = new ArrayList<>();
        con = ConectarBD.abrirConexao();
        sql = "SELECT CodigoBarras FROM Produto WHERE CodigoBarras LIKE ? ORDER BY CodigoBarras ASC";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + parte + "%");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                codigos.add(rs.getString("CodigoBarras"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigos;
    }
}
