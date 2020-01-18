package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.CategoriaDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Categoria;
import br.com.salao.util.StringUtil;

@Service("categoriaServico")
public class CategoriaServicoImpl implements CategoriaServico, Serializable {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private CategoriaDAO categoriaDAO;	
	private static Logger logg = Logger.getLogger(CategoriaServicoImpl.class);
	
	public CategoriaServicoImpl(){	
	}

	@Transactional(readOnly=true)
	@Override
	public List<Categoria> listar(boolean ativo) throws ServicoException {	
		List<Categoria> categorias = categoriaDAO.listar(ativo);	
		return categorias;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Categoria categoria) throws ServicoException {
		Categoria categoriaAntigo = null;		
		try {
			categoriaAntigo = categoriaDAO.buscar(categoria.getNome());	
			if(categoriaAntigo != null){
				logg.error("Categoria com nome '"+categoria.getNome()+"' já foi cadastrado");
				throw new ServicoException("Categoria com nome '"+categoria.getNome()+"' já foi cadastrado");
			}		
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(categoria.getNome().trim());	
			categoria.setNome(nome);
			categoriaDAO.salvar(categoria);		
		} catch (Exception e) {	
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Categoria categoria) throws ServicoException {		
		Categoria categoriaAntigo = null;		
		try {
			categoriaAntigo = categoriaDAO.buscar(categoria.getNome());
			if(categoriaAntigo != null && !categoria.getId().equals(categoriaAntigo.getId())){
				logg.error("Categoria com nome '"+categoria.getNome()+"' já foi cadastrado");
				throw new ServicoException("Categoria com nome '"+categoria.getNome()+"' já foi cadastrado");
			}				
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(categoria.getNome().trim());	
			categoria.setNome(nome);
			categoriaDAO.alterar(categoria);			
		} catch (Exception e) {	
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Categoria categoria) throws ServicoException {
		try{			
			long qtd= categoriaDAO.buscarQuantidadeProdutosServicos(categoria.getId());
			if(qtd > 0){
				categoria.setAtivo(false);
				categoriaDAO.alterar(categoria);
			}else{
				categoria = categoriaDAO.buscar(categoria.getId());
				categoriaDAO.excluir(categoria);
			}			
		}catch(Exception e){
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public Categoria buscar(int id) throws ServicoException {
		Categoria categoria = null;
		try {
			categoria = categoriaDAO.buscar(id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
		return categoria;
	}
		
}
