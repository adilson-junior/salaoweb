package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Profissional;


public interface ProfissionalDAO {
	
	Profissional buscar(Integer id);
	Profissional buscar(String senha, String celular);
	List<Profissional> listar(boolean ativo);
	List<Profissional> listar(boolean ativo, Date dataCadastro);
	List<Profissional> buscarProfissionaisEAgendas(boolean ativo, Date data);
	Profissional buscarProfissionalEAgendas(boolean ativo, Date data, int idProfissional);
	void salvar(Profissional cliente);
	void alterar(Profissional cliente);
	void excluir(Profissional cliente);
	List<Profissional> buscar(String palavraChave);
	Long buscarQuantidadeAgendas(Integer idCliente);
	Long buscarQuantidadeAgendas(Integer idProfissional, Date inicio);
	Long buscarAgendas(Integer idCliente, Date inicio, Date fim);
	List<Profissional> buscarProfissionaisEItensFaturamento(Date inicio, Date fim, List<Integer> ids);
	List<Profissional> listarProfissionalPagamentoComissao();
	Profissional buscarProfissionalEItensFaturamento(Date inicio, Date fim, Integer id);
}
