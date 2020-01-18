package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.ServicoDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Servico;
import br.com.salao.util.DateUtil;
import br.com.salao.util.StringUtil;

@Service("servicoServico")
public class ServicoServicoImpl implements ServicoServico , Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private ServicoDAO servicoDAO;
	private static Logger logg = Logger.getLogger(ServicoServicoImpl.class);
	
	public ServicoServicoImpl(){	
	}

	@Transactional(readOnly=true)
	@Override
	public Servico buscar(Integer id) throws ServicoException {		
		Servico servico = servicoDAO.buscar(id);			
		return servico;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Servico> listar(boolean ativo) throws ServicoException {		
		List<Servico> servicos = servicoDAO.listar(ativo);			
		return servicos;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Servico servico) throws ServicoException {		
		if(servico.getNome() == null || servico.getNome().trim().equals("") || servico.getPreco() == null || servico.getPreco() == 0){
			logg.error("Nome e preço devem ser informados.");
			throw new ServicoException("Nome e preço devem ser informados."); 
		}
		try {				
			if(servico.getDetalhes() != null && servico.getDetalhes().length() > 300){
				servico.setDetalhes(servico.getDetalhes().substring(0, 299));
			}		
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(servico.getNome().trim());
			servico.setNome(nome);
			servicoDAO.salvar(servico);	
		} catch (Exception e) {		
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Servico servico) throws ServicoException {
		if(servico.getNome() == null || servico.getNome().trim().equals("") || servico.getPreco() == null || servico.getPreco() == 0){
			logg.error("Nome e preço devem ser informados.");
			throw new ServicoException("Nome e preço devem ser informados."); 
		}
		try{							
			if(servico.getDetalhes() != null && servico.getDetalhes().length() > 300){
				servico.setDetalhes(servico.getDetalhes().substring(0, 299));
			}	
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(servico.getNome().trim());
			servico.setNome(nome);
			servicoDAO.alterar(servico);			
		}catch (Exception e) {	
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Servico servico) throws ServicoException {
		try{
			DateUtil dateUtil = new DateUtil();					
			long agendasFuturo = servicoDAO.buscarQuantidadeAgendas(servico.getId(), dateUtil.getDataHora(0, 0, 0));
			if(agendasFuturo > 0){
				logg.error("O servico "+servico.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
				throw new ServicoException("O servico "+servico.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
			}
			long agendasTotal = servicoDAO.buscarQuantidadeAgendas(servico.getId());
			int qtdProfissionais = servicoDAO.buscarQuantidadeProfissionais(servico.getId());
			if(agendasTotal > 0 || qtdProfissionais > 0){
				// Servicos com histórico de agendas não serão excluídos
				servico.setAtivo(false);				
				servicoDAO.alterar(servico);
			}else{
				servico = servicoDAO.buscar(servico.getId());
				servicoDAO.excluir(servico);
			}	
			
		}catch(Exception e){	
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Servico> buscar(String nome) throws ServicoException {
		if(nome == null){
			logg.error("Informe um valor para a pesquisa.");
			throw new ServicoException("Informe um valor para a pesquisa.");
		}
		List<Servico> servicos = null;
		try {
			servicos = servicoDAO.buscar(nome);
		} catch (Exception e) {
			logg.error(e);
		}		
		return servicos;
	}

}
