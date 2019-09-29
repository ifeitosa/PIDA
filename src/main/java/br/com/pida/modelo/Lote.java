/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author italo
 */
public class Lote implements Serializable {
    
    private int id;
    private double quantidade;
    private Date vencimento;
    
    public Lote() {
        id = -1;
        quantidade = 0.00d;
        vencimento = Date.from(Instant.now());
    }
    
    public Lote(int id, double quantidade, Date vencimento) {
        this.id= id;
        this.quantidade = quantidade;
        this.vencimento = vencimento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        hash = 43 * hash + Objects.hashCode(this.vencimento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lote other = (Lote) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }
    
}