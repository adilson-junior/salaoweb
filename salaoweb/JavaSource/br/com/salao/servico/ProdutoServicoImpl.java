package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.ProdutoDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Produto;
import br.com.salao.util.StringUtil;

@Service("produtoServico")
public class ProdutoServicoImpl implements ProdutoServico, Serializable {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private ProdutoDAO produtoDAO;
	private static Logger logg = Logger.getLogger(ProdutoServicoImpl.class);
	
	public ProdutoServicoImpl(){	
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Produto produto) throws ServicoException {
		Produto produtoAntigo = null;		
		try {
			produtoAntigo = produtoDAO.buscarPorNome(produto.getNome());	
			if(produtoAntigo != null){
				logg.error("Produto com nome '"+produto.getNome()+"' já foi cadastrado");
				throw new ServicoException("Produto com nome '"+produto.getNome()+"' já foi cadastrado");
			}			
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(produto.getNome().trim());
			produto.setNome(nome);
			produtoDAO.salvar(produto);			
		} catch (Exception e) {			
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Produto produto) throws ServicoException {		
		Produto produtoAntigo = null;		
		try {
			produtoAntigo = produtoDAO.buscarPorNome(produto.getNome());
			if(produtoAntigo != null && !produto.getId().equals(produtoAntigo.getId())){
				logg.error("Produto com nome '"+produto.getNome()+"' já foi cadastrado");
				throw new ServicoException("Produto com nome '"+produto.getNome()+"' já foi cadastrado");
			}	
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(produto.getNome().trim());
			produto.setNome(nome);
			produtoDAO.alterar(produto);			
		} catch (Exception e) {		
			logg.error(e);
			throw new ServicoException(e.getMessage());			
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Produto produto) throws ServicoException {
		try{			
			long qtdCheques = produtoDAO.buscarQuantidadeServicos(produto.getId());
			if(qtdCheques > 0){
				produto.setAtivo(false);
				produtoDAO.alterar(produto);
			}else{
				produto = produtoDAO.buscar(produto.getId());
				produtoDAO.excluir(produto);
			}		
		}catch(Exception e){
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public Produto buscar(int id) throws ServicoException {
		Produto produto = null;
		try {
			produto = produtoDAO.buscar(id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return produto;
	}	

	@Transactional(readOnly=true)
	@Override
	public List<Produto> listar(boolean ativo) throws ServicoException {
		List<Produto> produtos = null;
		try {
			produtos = produtoDAO.listar(ativo);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return produtos;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Produto> buscar(String nome) throws ServicoException {
		List<Produto> produtos = null;
		try {
			produtos = produtoDAO.buscar(nome);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return produtos;
	}

}
