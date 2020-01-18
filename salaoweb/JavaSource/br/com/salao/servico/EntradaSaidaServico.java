package br.com.salao.servico;

import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Vale;

public interface EntradaSaidaServico {
	void salvarReforco(Reforco reforco) throws ServicoException;
	void alterarReforco(Reforco reforco) throws ServicoException;
	void excluirReforco(Reforco reforco) throws ServicoException;
	List<Reforco> listarReforcos(Integer idCaixa) throws ServicoException;
	void salvarSangria(Sangria sangria) throws ServicoException;
	void alterarSangria(Sangria sangria) throws ServicoException;
	void excluirSangria(Sangria sangria) throws ServicoException;
	List<Sangria> listarSangrias(Integer idCaixa) throws ServicoException;
	void salvarVale(Vale vale) throws ServicoException;
	void alterarVale(Vale vale) throws ServicoException;
	void excluirVale(Vale vale) throws ServicoException;
	List<Vale> listarVales(Date inicio, Date fim) throws ServicoException;
	List<Vale> listarVales(Integer idProfissional, Date inicio, Date fim) throws ServicoException;
	List<Reforco> listarReforcos(Date inicio, Date fim) throws ServicoException;
	List<Sangria> listarSangrias(Date inicio, Date fim) throws ServicoException;
	void salvarOutros(Outros outros) throws ServicoException;
	void alterarOutros(Outros outros) throws ServicoException;
	void excluirOutros(Outros outros) throws ServicoException;	
	List<Outros> listarOutross(Date inicio, Date fim) throws ServicoException;
	List<Outros> listarOutross(Integer idProfissional, Date inicio, Date fim) throws ServicoException;
}
