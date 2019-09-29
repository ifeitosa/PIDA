/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author italo
 */
public class Transação {
    private int id;
    private Date data;
    private List<ItemTransação> itens;
    
    public Transação() {
        id = -1;
        data = new Date();
        itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<ItemTransação> getItens() {
        return itens;
    }

    public void setItens(List<ItemTransação> itens) {
        this.itens = itens;
    }
}
