package br.com.salao.dao;

import java.util.List;

import br.com.salao.modelo.Bandeira;
import br.com.salao.modelo.Cartao;

public interface BandeiraDAO {
	List<Bandeira> listar(boolean ativo);
	Bandeira buscar(String nome);
	Bandeira buscar(int id);
	void salvar(Bandeira bandeira);
	void alterar(Bandeira bandeira);
	void excluir(Bandeira bandeira);
	long buscarQuantidadeCartoes(int id);
	void salvarCartao(Cartao cartao);
}
