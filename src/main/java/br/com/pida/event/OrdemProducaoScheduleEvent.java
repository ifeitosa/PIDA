/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.event;

import br.com.pida.modelo.OrdemProducao;
import java.util.Date;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author italo
 */
public class OrdemProducaoScheduleEvent extends DefaultScheduleEvent {
    
    private OrdemProducao ordem;
    
    public OrdemProducaoScheduleEvent(OrdemProducao ordem) {
        this.ordem = ordem;
    }
    
    @Override
    public String getTitle() {
        return ordem.getReceita().getNome() + " " + ordem.getQuantidade();
    }
    
    @Override
    public Date getStartDate() {
        return ordem.getDataRegistro();
    }
    
    @Override
    public Date getEndDate() {
        return ordem.getDataRegistro();
    }
    
    @Override
    public boolean isAllDay() {
        return true;
    }
    
    public OrdemProducao getOrdemProducao() {
        return this.ordem;
    }
    
    @Override
    public String getDescription() {
        return this.ordem.getReceita().getNome() + " " + ordem.getQuantidade();
    }
    
}
