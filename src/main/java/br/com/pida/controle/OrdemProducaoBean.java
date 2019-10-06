/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.dao.OrdemProducaoDAO;
import br.com.pida.dao.ReceitaDAO;
import br.com.pida.modelo.OrdemProducao;
import br.com.pida.modelo.Produto;
import br.com.pida.modelo.Receita;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author italo
 */
@ManagedBean(name="ordemProducao")
@SessionScoped
public class OrdemProducaoBean implements Serializable {
    
    private OrdemProducao ordem;
    
    public OrdemProducaoBean() {
        ordem = new OrdemProducao();
    }

    public OrdemProducao getOrdem() {
        return ordem;
    }

    public void setOrdem(OrdemProducao ordem) {
        this.ordem = ordem;
    }
    
    public List<String> listaDeReceitas() {
        System.out.println("listaDeReceitas()");
        ReceitaDAO receitaDAO = new ReceitaDAO();
        return receitaDAO.pesquisarReceitas();
    }
    
    public void emitirOrdem() {
        OrdemProducaoDAO dao = new OrdemProducaoDAO();
        ReceitaDAO receitaDAO = new ReceitaDAO();
        Receita receita = receitaDAO.obterReceita(ordem.getReceita().getNome());
        ordem.setReceita(receita);
        List<Produto> produtosFaltantes = dao.verificaOrdem(ordem);
        if (produtosFaltantes.size() > 0) {
            FacesMessage mensagem = new FacesMessage("Produtos em falta", formatarMensagem(produtosFaltantes));
            FacesContext.getCurrentInstance().addMessage("Produtos faltantes", mensagem);
        } else {
            dao.inserirOrdem(ordem);
            FacesMessage mensagem = new FacesMessage("Ordem emitida com sucesso", "Ordem emitida com sucesso");
            FacesContext.getCurrentInstance().addMessage("Ordem emitida", mensagem);
            ordem = new OrdemProducao();
        }
        
    }
    
    private String formatarMensagem(List<Produto> produtosFaltantes) {
        String resultado = "";
        for(Produto produto : produtosFaltantes) {
            resultado += produto.getProduto() + "\n";
        }
        return resultado;
    }
    
}
