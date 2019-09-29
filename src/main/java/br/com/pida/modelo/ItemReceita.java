/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

import br.com.pida.DAO.ProdutoDAO;
import java.io.Serializable;

/**
 *
 * @author italo
 */
public class ItemReceita implements Serializable {
    
    private int produtoId;
    private String produto;
    private double quantidade;
    private String unidade;

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        ProdutoDAO dao = new ProdutoDAO();
        this.produto = produto;
        setProdutoId(dao.obterProduto(produto));
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }
    
    
}
