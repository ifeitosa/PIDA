 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author italo
 */
public class Produto implements Serializable {
    
    private int id;
    private String codigoBarras;
    private String produto;
    private Double preçoVenda;
    private List<Lote> lotes;
    
    public Produto() {
        id = -1;
        codigoBarras = "";
        produto = "";
        preçoVenda = 0.00;
        lotes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Double getPreçoVenda() {
        return preçoVenda;
    }

    public void setPreçoVenda(Double preçoVenda) {
        this.preçoVenda = preçoVenda;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.codigoBarras);
        hash = 53 * hash + Objects.hashCode(this.produto);
        hash = 53 * hash + Objects.hashCode(this.preçoVenda);
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
        final Produto other = (Produto) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.codigoBarras, other.codigoBarras)) {
            return false;
        }
        if (!Objects.equals(this.produto, other.produto)) {
            return false;
        }
        if (!Objects.equals(this.preçoVenda, other.preçoVenda)) {
            return false;
        }
        return true;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }
    
    public boolean codigoDeBarrasVálido() {
        if (codigoBarras.trim().length() != 13) {
            return false;
        }
        for(int i = 0; i < codigoBarras.length(); i++) {
            final char c = codigoBarras.charAt(i);
            if ((c >= '0') && (c <= '9')) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
