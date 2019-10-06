/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.dao;

import br.com.pida.conexao.ConectarBD;
import br.com.pida.modelo.Produto;
import br.com.pida.modelo.ResumoEstoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author italo
 */
public class ResumoEstoqueDAO {
    
    public List<ResumoEstoque> obterListaEstoque() {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Connection con = ConectarBD.abrirConexao();
        String sql = "SELECT P.CodigoBarras, P.Produto, P.PrecoVenda, SUM(L.Quantidade) AS Estoque FROM Produto AS P JOIN Lote AS L ON P.IdProduto = L.Produto GROUP BY P.Produto ORDER BY Estoque ASC";
        List<ResumoEstoque> itens = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Produto produto = produtoDAO.pesquisarProduto(rs.getString("CodigoBarras"));
                double precoVenda = rs.getDouble("PrecoVenda");
                double estoque = rs.getDouble("Estoque");
                ResumoEstoque resumo = new ResumoEstoque(produto, precoVenda, estoque);
                itens.add(resumo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResumoEstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }
}
