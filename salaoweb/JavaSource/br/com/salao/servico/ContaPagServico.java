package br.com.salao.servico;

import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.ContaPag;

public interface ContaPagServico {
	List<ContaPag> listar(Date inicio, Date fim, String colunaPesquisa) throws ServicoException;
	List<ContaPag> listarAtrasadas() throws ServicoException;
	List<ContaPag> listarAVencer() throws ServicoException;
	ContaPag buscar(int id) throws ServicoException;
	void salvar(ContaPag contaPag) throws ServicoException;
	void alterar(ContaPag contaPag) throws ServicoException;
	void excluir(ContaPag contaPag) throws ServicoException;
}
