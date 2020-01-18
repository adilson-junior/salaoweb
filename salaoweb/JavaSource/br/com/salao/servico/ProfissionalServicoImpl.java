package br.com.salao.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.ProfissionalDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Profissional;
import br.com.salao.util.DateUtil;
import br.com.salao.util.StringUtil;

@Service("profissionalServico")
public class ProfissionalServicoImpl implements ProfissionalServico, Serializable {	
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private ProfissionalDAO profissionalDAO;
	private static Logger logg = Logger.getLogger(ProfissionalServicoImpl.class);
	
	public ProfissionalServicoImpl(){		
	}

	@Transactional(readOnly=true)
	@Override
	public Profissional buscar(Integer id) throws ServicoException {		
		Profissional profissional = profissionalDAO.buscar(id);	
		return profissional;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> listar(boolean ativo) throws ServicoException {		
		List<Profissional> profissionais = profissionalDAO.listar(ativo);	
		return profissionais;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Profissional profissional) throws ServicoException {		
		if((profissional.getTelefone() == null || profissional.getTelefone().trim().equals(""))
				&& (profissional.getCelular() == null || profissional.getCelular().trim().equals(""))
				|| (profissional.getNome() == null || profissional.getNome().trim().equals(""))){
			logg.error("Nome e telefone ou celeluar devem ser informados.");
			throw new ServicoException("Nome e telefone ou celeluar devem ser informados."); 
		}
		try {			
			profissional.setSenha("123456");
			profissional.setDataCadastro(new Date());
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(profissional.getNome().trim());
			profissional.setNome(nome);
			if(profissional.getEmail() != null){	
				profissional.setEmail(profissional.getEmail().toLowerCase().trim());	
			}
			profissionalDAO.salvar(profissional);			
		} catch (Exception e) {		
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Profissional profissional) throws ServicoException {
		if((profissional.getTelefone() == null || profissional.getTelefone().trim().equals(""))
				&& (profissional.getCelular() == null || profissional.getCelular().trim().equals(""))
				|| (profissional.getNome() == null || profissional.getNome().trim().equals(""))){
			logg.error("Nome e telefone ou celeluar devem ser informados.");
			throw new ServicoException("Nome e telefone ou celeluar devem ser informados."); 
		}	
		try{			
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(profissional.getNome().trim());
			profissional.setNome(nome);
			if(profissional.getEmail() != null){	
				profissional.setEmail(profissional.getEmail().toLowerCase().trim());	
			}
			profissionalDAO.alterar(profissional);		
		}catch (Exception e) {	
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Profissional profissional) throws ServicoException {
		try{
			DateUtil dateUtil = new DateUtil();					
			long agendasFuturo = profissionalDAO.buscarQuantidadeAgendas(profissional.getId(), dateUtil.getDataHora(0, 0, 0));
			if(agendasFuturo > 0){
				logg.error("O profissional "+profissional.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
				throw new ServicoException("O profissional "+profissional.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
			}
			long agendasTotal = profissionalDAO.buscarQuantidadeAgendas(profissional.getId());
			if(agendasTotal > 0){
				// Profissionals com histórico de agendas não serão excluídos
				profissional.setAtivo(false);				
				profissionalDAO.alterar(profissional);
			}else{
				profissional = profissionalDAO.buscar(profissional.getId());
				profissionalDAO.excluir(profissional);
			}					
		}catch(Exception e){
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> buscar(String palavraChave) throws ServicoException {
		if(palavraChave == null){
			logg.error("Informe um valor para a pesquisa.");
			throw new ServicoException("Informe um valor para a pesquisa.");
		}
		List<Profissional> profissionais = null;
		try {
			profissionais = profissionalDAO.buscar(palavraChave);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
		return profissionais;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> buscarProfissionaisEAgendas(boolean ativo, Date data)	throws ServicoException {
		List<Profissional> profissionais = null;
		try {
			profissionais = profissionalDAO.buscarProfissionaisEAgendas(ativo, data);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}				
		return profissionais;
	}

	@Transactional(readOnly=true)
	@Override
	public Profissional buscar(String senha, String celular) throws ServicoException {
		Profissional profissional = null;
		try {
			profissional = profissionalDAO.buscar(senha, celular);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return profissional;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> buscarProfissionaisEItensFaturamento(Date inicio, Date fim, List<Integer> ids) throws ServicoException {
		List<Profissional> profissionais = null;
		try {
			profissionais = profissionalDAO.buscarProfissionaisEItensFaturamento(inicio, fim, ids);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return profissionais;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> listar(boolean ativo, Date dataCadastro) throws ServicoException {
		List<Profissional> profissionais = null;
		try {
			profissionais = profissionalDAO.listar(ativo, dataCadastro);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return profissionais;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Profissional> listarProfissionalPagamentoComissao()	throws ServicoException {
		List<Profissional> profissionais = null;
		try {
			profissionais = profissionalDAO.listarProfissionalPagamentoComissao();
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return profissionais;
	}

	@Transactional(readOnly=true)
	@Override
	public Profissional buscarProfissionalEItensFaturamento(Date inicio, Date fim, Integer id) throws ServicoException {		
		try {
			return profissionalDAO.buscarProfissionalEItensFaturamento(inicio, fim, id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}	
}
