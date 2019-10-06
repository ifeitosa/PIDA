/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.dao;

import br.com.pida.conexao.ConectarBD;
import br.com.pida.modelo.ItemTransação;
import br.com.pida.modelo.Transação;
import br.com.pida.util.DateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author italo
 */
public class TransaçãoDAO {
    
    public boolean inserirTransação(Transação transação) {
        Connection con = ConectarBD.abrirConexao();
        String sql = "INSERT INTO Transacao (Data) VALUES (?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, DateUtil.toDatabase(transação.getData()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transação.setId(rs.getInt(1));
                processarTransação(transação);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TransaçãoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    private void processarTransação(Transação transação) {
        Connection con = ConectarBD.abrirConexao();
        String sql = "INSERT INTO DetalheTransacao (Transacao, Produto, PreçoVenda, Quantidade) VALUES (?, ?, ?, ?)";
        List<ItemTransação> lista = transação.getItens();
        try {
            for(ItemTransação item : lista) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, transação.getId());
                ps.setInt(2, item.getProduto().getId());
                ps.setDouble(3, item.getPrecoVenda());
                ps.setDouble(4, item.getQuantidade());
                ps.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransaçãoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
