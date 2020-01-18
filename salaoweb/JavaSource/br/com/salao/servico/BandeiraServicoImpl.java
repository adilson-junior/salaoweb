package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.BandeiraDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Bandeira;
import br.com.salao.util.StringUtil;

@Service("bandeiraServico")
public class BandeiraServicoImpl implements BandeiraServico, Serializable {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private BandeiraDAO bandeiraDAO;	
	private static Logger logg = Logger.getLogger(BandeiraServicoImpl.class);
	
	public BandeiraServicoImpl(){		
	}

	@Transactional(readOnly=true)
	@Override
	public List<Bandeira> listar(boolean ativo) throws ServicoException {	
		List<Bandeira> bandeiras = null;
		try {
			bandeiras = bandeiraDAO.listar(ativo);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
		return bandeiras;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Bandeira bandeira) throws ServicoException {
		Bandeira bandeiraAntigo = null;		
		try {
			bandeiraAntigo = bandeiraDAO.buscar(bandeira.getNome());	
			if(bandeiraAntigo != null){
				logg.error("Bandeira com nome '"+bandeira.getNome()+"' já foi cadastrado");
				throw new ServicoException("Bandeira com nome '"+bandeira.getNome()+"' já foi cadastrado");
			}
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(bandeira.getNome().trim());	
			bandeira.setNome(nome);
			bandeiraDAO.salvar(bandeira);			
		} catch (Exception e) {				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Bandeira bandeira) throws ServicoException {		
		Bandeira bandeiraAntigo = null;		
		try {
			bandeiraAntigo = bandeiraDAO.buscar(bandeira.getNome());
			if(bandeiraAntigo != null && !bandeira.getId().equals(bandeiraAntigo.getId())){
				logg.error("Bandeira com nome '"+bandeira.getNome()+"' já foi cadastrado");
				throw new ServicoException("Bandeira com nome '"+bandeira.getNome()+"' já foi cadastrado");
			}	
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(bandeira.getNome().trim());	
			bandeira.setNome(nome);
			bandeiraDAO.alterar(bandeira);			
		} catch (Exception e) {						
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Bandeira bandeira) throws ServicoException {
		try{				
			long qtdCheques = bandeiraDAO.buscarQuantidadeCartoes(bandeira.getId());
			if(qtdCheques > 0){
				bandeira.setAtivo(false);
				bandeiraDAO.alterar(bandeira);
			}else{
				bandeira = bandeiraDAO.buscar(bandeira.getId());
				bandeiraDAO.excluir(bandeira);
			}		
		}catch(Exception e){				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public Bandeira buscar(int id) throws ServicoException {
		Bandeira bandeira = null;
		try {
			bandeira = bandeiraDAO.buscar(id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return bandeira;
	}
}
