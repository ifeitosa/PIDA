/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.event;

import br.com.pida.modelo.Lote;
import br.com.pida.modelo.Produto;
import java.util.Date;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author italo
 */
public class VencimentoScheduleEvent extends DefaultScheduleEvent {
    
    private Produto produto;
    private Lote lote;
    
    public VencimentoScheduleEvent(Produto produto, Lote lote) {
        this.produto = produto;
        this.lote = lote;
    }
    
    @Override
    public String getTitle() {
        return produto.getProduto() + " Lote " + lote.getId();
    }
    
    @Override
    public Date getStartDate() {
        return lote.getVencimento();
    }
    
    @Override
    public Date getEndDate() {
        return lote.getVencimento();
    }
    
    @Override
    public boolean isAllDay() {
        return true;
    }
    
    public Produto getProduto() {
        return this.produto;
    }
    
    public Lote getLote() {
        return this.lote;
    }
    
    @Override
    public String getDescription() {
        return this.produto.getProduto() + " Lote " + this.lote.getId();
    }
}
