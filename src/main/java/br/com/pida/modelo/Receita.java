/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author italo
 */
public class Receita implements Serializable {
    
    private int id;
    private String nome;
    private Date registro;
    private List<ItemReceita> itens;
    
    public Receita() {
        id = -1;
        nome = "";
        registro = new Date();
        itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public List<ItemReceita> getItens() {
        return itens;
    }

    public void setItens(List<ItemReceita> itens) {
        this.itens = itens;
    }
    
    
}
