/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.dao.ProdutoDAO;
import br.com.pida.dao.ReceitaDAO;
import br.com.pida.modelo.ItemReceita;
import br.com.pida.modelo.Receita;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author italo
 */
@ManagedBean(name = "receita")
@SessionScoped
public class ReceitaBean {

    private boolean novaReceita;
    private Receita receita;
    private ReceitaDAO receitaDAO;
    private ProdutoDAO produtoDAO;
    private ItemReceita itemReceita;

    public ReceitaBean() {
        novaReceita = true;
        receita = new Receita();
        itemReceita = new ItemReceita();
        receitaDAO = new ReceitaDAO();
        produtoDAO = new ProdutoDAO();
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public List<String> completar(String query) {
        return receitaDAO.pesquisarReceitas(query);
    }

    public void selecionarReceita() {
        receita = receitaDAO.obterReceita(receita.getNome());
        if (receita == null) {
            FacesMessage mensagem = new FacesMessage("Receita não cadastrada", "A receita indiciada não consta do banco de dados.");
            FacesContext.getCurrentInstance().addMessage("Receita não cadastrada", mensagem);
            receita = new Receita();
        } else {
            novaReceita = false;
        }
        
    }
    
    public List<String> getNomeProdutos() {
        return produtoDAO.obterListaProdutos();        
    }
    
    public void deletarItem(ItemReceita itemReceita) {
        receitaDAO.deletarItemReceita(receita, itemReceita);
        this.receita.getItens().remove(itemReceita);
    }
    
    public void rowEditHandler(RowEditEvent event) {
        ItemReceita item = (ItemReceita) event.getObject();
        receitaDAO.atualizarItemReceita(receita, item);
    }

    public ItemReceita getItemReceita() {
        return itemReceita;
    }

    public void setItemReceita(ItemReceita itemReceita) {
        this.itemReceita = itemReceita;
    }
    
    public void adicionarItemReceita() {
        for(ItemReceita item : receita.getItens()) {
            if (item.getProduto().equals(itemReceita.getProduto()) && item.getProdutoId() == itemReceita.getProdutoId()) {
                FacesMessage mensagem = new FacesMessage("Alerta", "Produto já consta da lista de itens da receita");
                FacesContext.getCurrentInstance().addMessage("Alerta", mensagem);
                return;
            }
        }
        receita.getItens().add(itemReceita);
        if (!novaReceita) {
            receitaDAO.adicionarItemReceita(receita, itemReceita);
        } else {
            receitaDAO.inserirReceita(receita);
            novaReceita = false;
        }
        itemReceita = new ItemReceita();
    }
    
    public void novaReceita() {
        novaReceita = true;
        receita = new Receita();
    }
}
