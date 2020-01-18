package br.com.salao.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.ContaPagDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.ContaPag;

@Service("contaPagServico")
public class ContaPagServicoImpl implements ContaPagServico, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@Autowired
	private ContaPagDAO contaPagDAO;
	private static Logger logg = Logger.getLogger(ContaPagServicoImpl.class);
	
	public ContaPagServicoImpl(){		
	}

	@Transactional(readOnly=true, rollbackFor=ServicoException.class)
	@Override
	public List<ContaPag> listar(Date inicio, Date fim, String colunaPesquisa) throws ServicoException{		
		try{
			return contaPagDAO.listar(inicio, fim, colunaPesquisa);
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
	}

	@Transactional(readOnly=true, rollbackFor=ServicoException.class)
	@Override
	public ContaPag buscar(int id) throws ServicoException{		
		try{
			return contaPagDAO.buscar(id);
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(ContaPag contaPag) throws ServicoException{
		try{
			contaPagDAO.salvar(contaPag);
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(ContaPag contaPag) throws ServicoException{
		try{
			contaPagDAO.alterar(contaPag);
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(ContaPag contaPag) throws ServicoException{
		try{
			contaPag = contaPagDAO.buscar(contaPag.getId());
			contaPagDAO.excluir(contaPag);
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<ContaPag> listarAtrasadas() throws ServicoException {	
		try{
			return contaPagDAO.listarAtrasadas();
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<ContaPag> listarAVencer() throws ServicoException {	
		try{
			return contaPagDAO.listarAVencer();
		}catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}
}
