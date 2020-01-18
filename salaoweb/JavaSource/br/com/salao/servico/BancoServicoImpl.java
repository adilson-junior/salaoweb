package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.BancoDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Banco;
import br.com.salao.util.StringUtil;

@Service("bancoServico")
public class BancoServicoImpl implements BancoServico, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@Autowired
	private BancoDAO bancoDAO;	
	private static Logger logg = Logger.getLogger(BancoServicoImpl.class);
	
	public BancoServicoImpl(){		
	}

	@Transactional(readOnly=true)
	@Override
	public List<Banco> listar(boolean ativo) throws ServicoException {	
		List<Banco> bancos = null;
		try {
			bancos = bancoDAO.listar(ativo);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
		return bancos;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Banco banco) throws ServicoException {
		Banco bancoAntigo = null;		
		try {
			bancoAntigo = bancoDAO.buscar(banco.getNome());	
			if(bancoAntigo != null){
				logg.error("Banco com nome '"+banco.getNome()+"' já foi cadastrado");
				throw new ServicoException("Banco com nome '"+banco.getNome()+"' jÃ¡ foi cadastrado");
			}	
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(banco.getNome().trim());	
			banco.setNome(nome);
			bancoDAO.salvar(banco);		
		} catch (Exception e) {					
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Banco banco) throws ServicoException {		
		Banco bancoAntigo = null;		
		try {
			bancoAntigo = bancoDAO.buscar(banco.getNome());
			if(bancoAntigo != null && !banco.getId().equals(bancoAntigo.getId())){
				logg.error("Banco com nome '"+banco.getNome()+"' já foi cadastrado");
				throw new ServicoException("Banco com nome '"+banco.getNome()+"' já foi cadastrado");
			}	
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(banco.getNome().trim());	
			banco.setNome(nome);
			bancoDAO.alterar(banco);			
		} catch (Exception e) {					
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Banco banco) throws ServicoException {
		try{			
			long qtdCheques = bancoDAO.buscarQuantidadeCheques(banco.getId());
			if(qtdCheques > 0){
				banco.setAtivo(false);
				bancoDAO.alterar(banco);
			}else{
				banco = bancoDAO.buscar(banco.getId());
				bancoDAO.excluir(banco);
			}		
		}catch(Exception e){				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public Banco buscar(int id) throws ServicoException {
		Banco banco = null;
		try {
			banco = bancoDAO.buscar(id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return banco;
	}	
}
