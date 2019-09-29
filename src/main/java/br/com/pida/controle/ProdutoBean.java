/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.DAO.ProdutoDAO;
import br.com.pida.modelo.Lote;
import br.com.pida.modelo.Produto;
import java.util.ArrayList;
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
@ManagedBean(name="produto")
@SessionScoped
public class ProdutoBean {
    private Produto produto;
    private Lote lote;
    private List<Lote> lotes;
    private final ProdutoDAO produtoDAO;
    private boolean novo;
    
    public ProdutoBean() {
        System.out.println("Criando ProdutoBean");
        novo = true;
        produto = new Produto();
        lote = new Lote();
        lotes = new ArrayList<>();
        produtoDAO = new ProdutoDAO();
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }
    
    private boolean verificaCodigoDeBarras() {
        if (!produto.codigoDeBarrasVálido()) {
            FacesMessage mensagem = new FacesMessage("Código de barras inválido", "O campo código de barras deve possuir 13 dígitos.");
            FacesContext.getCurrentInstance().addMessage("Código de barras inválido", mensagem);
            return false;
        }
        return true;
    }
    
    public void salvarProduto() {
        if (!verificaCodigoDeBarras()) return;
        if (novo) {
            System.out.println("Inserindo produto");
            if (produtoDAO.inserirProduto(produto)) {
                produto = new Produto();
                lote = new Lote();
                lotes = produto.getLotes();
                novo = false;
            }   
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto cadastrado", "Cadastro do produto criado com sucesso");
            FacesContext.getCurrentInstance().addMessage("Mensagem", message);
        } else {
            System.out.println("Atualizando produto");
            produtoDAO.atualizarProduto(produto);
            produto = new Produto();
            lote = new Lote();
            lotes = produto.getLotes();
            novo = true;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto atualizado", "Cadastro do produto atualizado com sucesso");
            FacesContext.getCurrentInstance().addMessage("Mensagem", message);
        }
    }
    
    private void debug(Lote lote) {
        System.out.println("Lote " + lote.getId() + " " + lote.getQuantidade() + " " + lote.getVencimento().toString());
    }
    
    public void buscarProduto() {
        if (!verificaCodigoDeBarras()) return;
        Produto novoProduto = produtoDAO.pesquisarProduto(produto.getCodigoBarras());
        if (novoProduto != null) {
            novo = false;
            produto = novoProduto;
            lotes = produto.getLotes();
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto não encontrado", "O produto não está cadastrado na base de dados.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            novo = true;
        }
    }
    
    public void onLoteRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Lote editado", "Lote " + ((Lote) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        produtoDAO.atualizarLote((Lote) event.getObject());
    }
    
    public void onLoteRowEditCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edição cancelada", "Lote " + ((Lote) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void adicionarLote() {
        produtoDAO.inserirLote(produto, lote);
        lotes.add(lote);
        lote = new Lote();
    }
}
