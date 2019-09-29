/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.DAO;

import br.com.pida.conexao.ConectarBD;
import br.com.pida.modelo.ItemReceita;
import br.com.pida.modelo.Lote;
import br.com.pida.modelo.OrdemProducao;
import br.com.pida.modelo.Produto;
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
public class OrdemProducaoDAO {

    /**
     * Insere a ordem de produção no banco de dados e atualiza estoques
     *
     * @param ordem
     *
     */
    public void inserirOrdem(OrdemProducao ordem) {
        ReceitaDAO receitaDAO = new ReceitaDAO();
        Connection con = ConectarBD.abrirConexao();
        String sql = "INSERT INTO OrdemProducao (Receita, Quantidade, DataRegistro) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ordem.getReceita().getId());
            assert(ordem.getQuantidade() > 0);
            ps.setInt(2, ordem.getQuantidade());
            ps.setDate(3, DateUtil.toDatabase(ordem.getDataRegistro()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ordem.setId(rs.getInt(1));
                processarOrdem(ordem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemProducaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processarOrdem(OrdemProducao ordem) {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        System.out.println("processarOrdem()");
        for (ItemReceita ir : ordem.getReceita().getItens()) {
            List<Lote> lotes = produtoDAO.obterLotes(ir.getProdutoId());
            assert(ordem.getQuantidade() > 0);
            double quantidade = ordem.getQuantidade() * ir.getQuantidade();
            System.out.println(ir.getProduto() + " " + quantidade);

            for (Lote lote : lotes) {
                System.out.println("Processando lote " + lote.getId() + " quantidade: " + lote.getQuantidade());
                if (quantidade > 0.0) {
                    if (lote.getQuantidade() > quantidade) {
                        System.out.println("Reduzindo lote " + lote.getId() + " para " + (lote.getQuantidade() - quantidade));
                        lote.setQuantidade(lote.getQuantidade() - quantidade);
                        produtoDAO.atualizarLote(lote);
                        quantidade = 0.0;
                    } else {
                        System.out.println("Zerando lote " + lote.getId());
                        quantidade -= lote.getQuantidade();
                        System.out.println("Nova quantidade " + quantidade);
                        lote.setQuantidade(0.0);
                        produtoDAO.atualizarLote(lote);
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Verifica se ordem de produção pode ser atendida, verificando a quantidade
     * de produtos necessários para sua fabricação.
     *
     * @param ordem ordem de produção
     * @return lista de produtos em falta.
     */
    public List<Produto> verificaOrdem(OrdemProducao ordem) {
        List<Produto> faltantes = new ArrayList<>();
        ProdutoDAO produtoDAO = new ProdutoDAO();
        System.out.println("verificaOrdem()");
        for (ItemReceita ir : ordem.getReceita().getItens()) {
            List<Lote> lotes = produtoDAO.obterLotes(ir.getProdutoId());
            double quantidade = ordem.getQuantidade() * ir.getQuantidade();

            for (Lote lote : lotes) {
                if (quantidade > 0.0) {
                    if (lote.getQuantidade() > quantidade) {
                        System.out.println("Zerando quantidade");
                        quantidade = 0.0;
                    } else {
                        System.out.println("Reduzindo quantidade para " + (quantidade - lote.getQuantidade()));
                        quantidade -= lote.getQuantidade();
                    }
                } else {
                    break;
                }
            }
            System.out.println("Quantidade restante " + quantidade);
            if (quantidade > 0.0) {
                System.out.println("Produto em falta " + ir.getProduto() + " de " + quantidade);
                Produto produto = produtoDAO.produtoPorNome(ir.getProduto());
                faltantes.add(produto);
            }
        }
        return faltantes;
    }
    
    public List<OrdemProducao> obterOrdensProducao(java.util.Date start, java.util.Date end) {
        java.sql.Date startDate = DateUtil.toDatabase(start);
        java.sql.Date endDate = DateUtil.toDatabase(end);
        List<OrdemProducao> ordens = new ArrayList<>();
        Connection con = ConectarBD.abrirConexao();
        String sql = "SELECT OP.ID As OrdemId, OP.Receita As OrdemReceita, OP.Quantidade as OrdemQuantidade, OP.DataRegistro AS OrdemData, R.Nome AS NomeReceita FROM OrdemProducao AS OP JOIN Receita AS R ON OP.Receita = R.IdReceita WHERE OP.DataRegistro BETWEEN ? AND ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();
            ReceitaDAO receitaDAO = new ReceitaDAO();
            while (rs.next()) {
                OrdemProducao ordem = new OrdemProducao();
                ordem.setId(rs.getInt("OrdemId"));
                ordem.setDataRegistro(DateUtil.fromDatabase(rs.getDate("OrdemData")));
                ordem.setQuantidade(rs.getInt("OrdemQuantidade"));
                Receita receita = receitaDAO.obterReceita(rs.getString("NomeReceita"));                
                ordem.setReceita(receita);
                ordens.add(ordem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemProducaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordens;
    }

}
