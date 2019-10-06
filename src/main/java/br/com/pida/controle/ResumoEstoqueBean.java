/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.dao.ResumoEstoqueDAO;
import br.com.pida.modelo.ResumoEstoque;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author italo
 */
@ManagedBean(name="estoque")
@ViewScoped
public class ResumoEstoqueBean implements Serializable {
    
    private List<ResumoEstoque> resumo;
    
    public ResumoEstoqueBean() {
        ResumoEstoqueDAO dao = new ResumoEstoqueDAO();
        resumo = dao.obterListaEstoque();
    }

    public List<ResumoEstoque> getResumo() {
        return resumo;
    }

    public void setResumo(List<ResumoEstoque> resumo) {
        this.resumo = resumo;
    }
    
}
