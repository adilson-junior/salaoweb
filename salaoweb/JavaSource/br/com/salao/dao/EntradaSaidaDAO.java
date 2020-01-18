package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Vale;

public interface EntradaSaidaDAO {
	void salvarReforco(Reforco reforco);
	void alterarReforco(Reforco reforco);
	void excluirReforco(Reforco reforco);
	List<Reforco> listarReforcos(Integer idCaixa);
	void salvarSangria(Sangria sangria);
	void alterarSangria(Sangria sangria);
	void excluirSangria(Sangria sangria);
	List<Sangria> listarSangrias(Integer idCaixa);
	void salvarVale(Vale vale);
	void alterarVale(Vale vale);
	void excluirVale(Vale vale);
	List<Vale> listarVales(Date inicio, Date fim);
	Vale buscarVale(Integer id);
	Sangria buscarSangria(Integer id);
	Reforco buscarReforco(Integer id);
	List<Reforco> listarReforcos(Date inicio, Date fim);
	List<Sangria> listarSangrias(Date inicio, Date fim);
	List<Vale> listarVales(Integer idProfissional, Date inicio, Date fim);	
	void salvarOutros(Outros outros);
	void alterarOutros(Outros outros);
	void excluirOutros(Outros outros);
	Outros buscarOutros(Integer id);
	List<Outros> listarOutross(Date inicio, Date fim);
	List<Outros> listarOutross(Integer idProfissional, Date inicio, Date fim);
}
