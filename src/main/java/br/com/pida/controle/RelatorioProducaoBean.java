/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;


import br.com.pida.DAO.OrdemProducaoDAO;
import br.com.pida.event.OrdemProducaoScheduleEvent;
import br.com.pida.modelo.OrdemProducao;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author italo
 */
@ManagedBean(name="relatorioProducao")
@SessionScoped
public class RelatorioProducaoBean {
    
    private ScheduleModel lazyModel;
    private OrdemProducaoDAO dao;
    
    public RelatorioProducaoBean() {
        dao = new OrdemProducaoDAO();
        lazyModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                List<OrdemProducao> ordens = dao.obterOrdensProducao(start, end);
                for(OrdemProducao ordem : ordens) {
                    ScheduleEvent event = new OrdemProducaoScheduleEvent(ordem);
                    addEvent(event);
                }
            }
        };
    }
    
    
    public ScheduleModel getLazyModel() {
        return lazyModel;
    }
}
