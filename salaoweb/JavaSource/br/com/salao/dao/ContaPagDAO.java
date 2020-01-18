package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.ContaPag;

public interface ContaPagDAO {
	List<ContaPag> listar(Date inicio, Date fim, String colunaPesquisa);
	List<ContaPag> listarAtrasadas();
	List<ContaPag> listarAVencer();
	ContaPag buscar(int id);
	void salvar(ContaPag contaPag);
	void alterar(ContaPag contaPag);
	void excluir(ContaPag contaPag);
}