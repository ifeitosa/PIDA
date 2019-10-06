
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
public class ItemTransação {
    
    private Transação transacao;
    private Produto produto;
    private double precoVenda;
    private double quantidade;
    
    public ItemTransação(Transação transação, Produto produto, double quantidade) {
        transacao = transação;
        this.produto = produto;
        precoVenda = produto.getPrecoVenda();
        this.quantidade = quantidade;
    }

    public Transação getTransacao() {
        return transacao;
    }

    public void setTransacao(Transação transacao) {
        this.transacao = transacao;
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

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    
}
