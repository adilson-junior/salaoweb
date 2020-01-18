package br.com.salao.servico;

import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Profissional;

public interface ProfissionalServico {	
	Profissional buscar(Integer id) throws ServicoException;	
	Profissional buscar(String senha, String celular) throws ServicoException;
	List<Profissional> listar(boolean ativo) throws ServicoException;
	List<Profissional> listar(boolean ativo, Date dataCadastro) throws ServicoException;
	List<Profissional> buscarProfissionaisEAgendas(boolean ativo, Date data) throws ServicoException;
	List<Profissional> buscar(String palavraChave) throws ServicoException;
	void salvar(Profissional profissional) throws ServicoException;
	void alterar(Profissional profissional) throws ServicoException;
	void excluir(Profissional profissional) throws ServicoException;	
	List<Profissional> buscarProfissionaisEItensFaturamento(Date inicio, Date fim, List<Integer> ids) throws ServicoException;
	List<Profissional> listarProfissionalPagamentoComissao() throws ServicoException;
	Profissional buscarProfissionalEItensFaturamento(Date inicio, Date fim, Integer id) throws ServicoException;
}
