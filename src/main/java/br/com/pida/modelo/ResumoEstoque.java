/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

/**
 *
 * @author italo
 */
public class ResumoEstoque {
    
    private Produto produto;
    private double precoVenda;
    private double estoque;
    
    public ResumoEstoque(Produto produto, double precoVenda, double estoque) {
        this.produto = produto;
        this.precoVenda = precoVenda;
        this.estoque = estoque;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getEstoque() {
        return estoque;
    }

    public void setEstoque(double estoque) {
        this.estoque = estoque;
    }
    
}
