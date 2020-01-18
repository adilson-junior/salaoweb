package br.com.salao.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.EntradaSaidaDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Vale;

@Service("entradaSaidaServico")
public class EntradaSaidaServicoImpl implements EntradaSaidaServico, Serializable {	
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private EntradaSaidaDAO entradaSaidaDAO;
	private static Logger logg = Logger.getLogger(EntradaSaidaServicoImpl.class);

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarReforco(Reforco reforco) throws ServicoException {
		try {
			entradaSaidaDAO.salvarReforco(reforco);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterarReforco(Reforco reforco) throws ServicoException {
		try {
			entradaSaidaDAO.alterarReforco(reforco);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluirReforco(Reforco reforco) throws ServicoException {
		try {
			reforco = entradaSaidaDAO.buscarReforco(reforco.getId());
			entradaSaidaDAO.excluirReforco(reforco);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Reforco> listarReforcos(Integer idCaixa) throws ServicoException {		
		try {
			return entradaSaidaDAO.listarReforcos(idCaixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarSangria(Sangria sangria) throws ServicoException {
		try {
			entradaSaidaDAO.salvarSangria(sangria);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterarSangria(Sangria sangria) throws ServicoException {
		try {
			entradaSaidaDAO.alterarSangria(sangria);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluirSangria(Sangria sangria) throws ServicoException {
		try {
			sangria = entradaSaidaDAO.buscarSangria(sangria.getId());
			entradaSaidaDAO.excluirSangria(sangria);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Sangria> listarSangrias(Integer idCaixa) throws ServicoException {
		try {
			return entradaSaidaDAO.listarSangrias(idCaixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarVale(Vale vale) throws ServicoException {
		try {
			entradaSaidaDAO.salvarVale(vale);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterarVale(Vale vale) throws ServicoException {
		try {
			entradaSaidaDAO.alterarVale(vale);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluirVale(Vale vale) throws ServicoException {
		try {
			vale = entradaSaidaDAO.buscarVale(vale.getId());
			entradaSaidaDAO.excluirVale(vale);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Vale> listarVales(Date inicio, Date fim) throws ServicoException {		
		try {
			return entradaSaidaDAO.listarVales(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<Vale> listarVales(Integer idProfissional, Date inicio, Date fim) throws ServicoException {
		try {
			return entradaSaidaDAO.listarVales(idProfissional, inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<Reforco> listarReforcos(Date inicio, Date fim)	throws ServicoException {
		List<Reforco> reforcos = null;
		try {
			reforcos = entradaSaidaDAO.listarReforcos(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return reforcos;
	}

	@Override
	public List<Sangria> listarSangrias(Date inicio, Date fim)	throws ServicoException {
		List<Sangria> sangrias = null;
		try {
			sangrias = entradaSaidaDAO.listarSangrias(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return sangrias;
	}
	
	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarOutros(Outros outros) throws ServicoException {
		try {
			entradaSaidaDAO.salvarOutros(outros);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterarOutros(Outros outros) throws ServicoException {
		try {
			entradaSaidaDAO.alterarOutros(outros);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluirOutros(Outros outros) throws ServicoException {
		try {
			outros = entradaSaidaDAO.buscarOutros(outros.getId());
			entradaSaidaDAO.excluirOutros(outros);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(readOnly=true)
	@Override
	public List<Outros> listarOutross(Date inicio, Date fim) throws ServicoException {		
		try {
			return entradaSaidaDAO.listarOutross(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<Outros> listarOutross(Integer idProfissional, Date inicio, Date fim) throws ServicoException {
		try {
			return entradaSaidaDAO.listarOutross(idProfissional, inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}	
}
