/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.controle;

import br.com.pida.dao.ProdutoDAO;
import br.com.pida.dao.TransaçãoDAO;
import br.com.pida.modelo.ItemTransação;
import br.com.pida.modelo.Produto;
import br.com.pida.modelo.Transação;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author italo
 */
@ManagedBean(name = "caixa")
@SessionScoped
public class CaixaBean {

    private String codigoBarras;
    private Transação transacao;
    private List<ItemTransação> itens;
    private Map<Produto, ItemTransação> tabela;

    public CaixaBean() {
        codigoBarras = "";
        transacao = new Transação();
        itens = transacao.getItens();
        tabela = new HashMap<>();
    }

    public void novaCompra() {
        codigoBarras = "";
        transacao = new Transação();
        itens = transacao.getItens();
        tabela = new HashMap<>();
    }

    public void finalizarCompra() {
        TransaçãoDAO dao = new TransaçãoDAO();
        dao.inserirTransação(transacao);
        novaCompra();
        FacesMessage mensagem = new FacesMessage("Novo cliente", "Iniciando atendimento do próximo cliente.");
        FacesContext.getCurrentInstance().addMessage("Próximo cliente", mensagem);
    }

    public List<String> completarCodigoBarras(String query) {
        ProdutoDAO dao = new ProdutoDAO();
        return dao.obterCodigosBarras(query);
    }

    public void adicionarItem() {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.pesquisarProduto(codigoBarras);
        if (produto == null) {
            FacesMessage mensagem = new FacesMessage("Produto não cadastrado", "Produto não encontrado no banco de dados.");
            FacesContext.getCurrentInstance().addMessage("Produto não cadastrado", mensagem);
        } else {
            if (tabela.containsKey(produto)) {
                ItemTransação item = tabela.get(produto);
                item.setQuantidade(item.getQuantidade() + 1.0);
            } else {
                ItemTransação item = new ItemTransação(transacao, produto, 1.0);
                tabela.put(produto, item);
                itens.add(item);
            }
        }
    }

    public void estornarItem() {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.pesquisarProduto(codigoBarras);
        if (produto == null) {
            FacesMessage mensagem = new FacesMessage("Produto não cadastrado", "Produto noã consta do banco de dados de produtos.");
            FacesContext.getCurrentInstance().addMessage("Produto não cadastrado", mensagem);
        } else {
            if (tabela.containsKey(produto)) {
                ItemTransação item = tabela.get(produto);
                item.setQuantidade(item.getQuantidade() - 1.0);
                if (item.getQuantidade() == 0.0) {
                    itens.remove(item);
                    tabela.remove(produto);
                }
            } else {
                FacesMessage mensagem = new FacesMessage("Produto não adicionado", "O produto não foi adicionar ao carrinho e não pode ser estornado.");
                FacesContext.getCurrentInstance().addMessage("Produto não adicionado", mensagem);
            }
        }
    }

    public double getTotal() {
        double total = 0.0;
        for (ItemTransação item : itens) {
            total += item.getPrecoVenda() * item.getQuantidade();
        }
        total = Math.round(total * 100.0) / 100.00;
        return total;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<ItemTransação> getItens() {
        return itens;
    }

    public void setItens(List<ItemTransação> itens) {
        this.itens = itens;
    }
}
