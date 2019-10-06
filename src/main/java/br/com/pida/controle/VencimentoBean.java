/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.dao.ProdutoDAO;
import br.com.pida.event.VencimentoScheduleEvent;
import br.com.pida.modelo.Lote;
import br.com.pida.modelo.Produto;
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
@ManagedBean(name = "vencimento")
@SessionScoped
public class VencimentoBean {

    private ScheduleModel lazyModel;
    private ProdutoDAO produtoDAO;

    public VencimentoBean() {
        produtoDAO = new ProdutoDAO();
        lazyModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                List<Produto> produtos = produtoDAO.obterVencimentos(start, end);
                for (Produto p : produtos) {
                    for (Lote l : p.getLotes()) {
                        ScheduleEvent event = new VencimentoScheduleEvent(p, l);
                        addEvent(event);
                    }
                }
            }
        };
    }

    public ScheduleModel getLazyModel() {
        return lazyModel;
    }
}
