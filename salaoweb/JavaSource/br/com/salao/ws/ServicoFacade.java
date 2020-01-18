package br.com.salao.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.com.salao.exception.ServicoException;
import br.com.salao.managedbean.ResourceManagedBean;
import br.com.salao.modelo.Banco;
import br.com.salao.modelo.Bandeira;
import br.com.salao.modelo.Categoria;
import br.com.salao.modelo.Cliente;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Produto;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Servico;
import br.com.salao.modelo.Usuario;
import br.com.salao.servico.BancoServico;
import br.com.salao.servico.BandeiraServico;
import br.com.salao.servico.CategoriaServico;
import br.com.salao.servico.ClienteServico;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProdutoServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.ServicoServico;
import br.com.salao.servico.UsuarioServico;

import com.sun.jersey.spi.resource.Singleton;

@Path("/rest")
@Singleton
public class ServicoFacade {
	
	private static Logger logg = Logger.getLogger(ServicoFacade.class);
	
	public ServicoFacade(){		
	}
	
	@Path("listarClientes")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Cliente> listarClientes() throws ServicoException{		
		List<br.com.salao.ws.xml.modelo.Cliente> clientes = new ArrayList<br.com.salao.ws.xml.modelo.Cliente>();
		try {			
			ClienteServico clienteServico = (ClienteServico) ResourceManagedBean.getServico("clienteServico");
			List<Cliente> cls = clienteServico.listar(true);
			for(Cliente cli : cls){
				br.com.salao.ws.xml.modelo.Cliente cliente = new br.com.salao.ws.xml.modelo.Cliente();
				cliente.setId(cli.getId());
				cliente.setAtivo(cli.getAtivo());
				cliente.setCelular(cli.getCelular());
				cliente.setEmail(cli.getEmail());
				cliente.setNome(cli.getNome());
				cliente.setTelefone(cli.getTelefone());
				clientes.add(cliente);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}		
		return clientes;
	}
	
	@Path("listarProfissionais")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Profissional> listarProfissionais() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Profissional> profissionais = new ArrayList<br.com.salao.ws.xml.modelo.Profissional>();
		try {
			ProfissionalServico profissionalServico = (ProfissionalServico) ResourceManagedBean.getServico("profissionalServico");
			List<Profissional> profis = profissionalServico.listar(true);
			for(Profissional prof :profis){
				br.com.salao.ws.xml.modelo.Profissional profissional = new br.com.salao.ws.xml.modelo.Profissional();
				profissional.setId(prof.getId());
				profissional.setAtivo(prof.getAtivo());
				profissional.setCelular(prof.getCelular());
				profissional.setEmail(prof.getEmail());
				profissional.setNome(prof.getNome());
				profissional.setTelefone(prof.getTelefone());
				profissional.setDataCadastro(prof.getDataCadastro());
				profissional.setSalarioFixo(prof.getSalarioFixo());
				profissionais.add(profissional);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return profissionais;
	}
	
	@Path("listarCategorias")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Categoria> listarCategorias() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Categoria> categorias = new ArrayList<br.com.salao.ws.xml.modelo.Categoria>();
		try {
			CategoriaServico categoriaServico = (CategoriaServico) ResourceManagedBean.getServico("categoriaServico");
			List<Categoria> cats = categoriaServico.listar(true);
			for(Categoria cat : cats){
				br.com.salao.ws.xml.modelo.Categoria categoria = new br.com.salao.ws.xml.modelo.Categoria();
				categoria.setId(cat.getId());
				categoria.setAtivo(cat.getAtivo());
				categoria.setNome(cat.getNome());
				categorias.add(categoria);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return categorias;
	}
	
	@Path("listarProdutos")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Produto> listarProdutos() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Produto> produtos = new ArrayList<br.com.salao.ws.xml.modelo.Produto>();
		try {
			ProdutoServico produtoServico = (ProdutoServico) ResourceManagedBean.getServico("produtoServico");
			List<Produto> prods = produtoServico.listar(true);
			for(Produto prd : prods){
				br.com.salao.ws.xml.modelo.Produto produto = new br.com.salao.ws.xml.modelo.Produto();
				produto.setAtivo(prd.getAtivo());
				produto.setComissao(prd.getComissao());
				produto.setCusto(prd.getCusto());
				produto.setId(prd.getId());
				produto.setNome(prd.getNome());
				produto.setParticipacao(prd.getParticipacao());
				produto.setPreco(prd.getPreco());
				produto.setQuantidade(prd.getQuantidade());
				produtos.add(produto);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return produtos;
	}
	
	@Path("listarServicos")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Servico> listarServicos() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Servico> servicos = new ArrayList<br.com.salao.ws.xml.modelo.Servico>();
		try {
			ServicoServico servicoServico = (ServicoServico) ResourceManagedBean.getServico("servicoServico");
			List<Servico> servs = servicoServico.listar(true);
			for(Servico sv : servs){
				br.com.salao.ws.xml.modelo.Servico servico = new br.com.salao.ws.xml.modelo.Servico();
				servico.setAtivo(sv.getAtivo());
				servico.setComissao(sv.getComissao());
				servico.setDetalhes(sv.getDetalhes());
				servico.setDuracao(sv.getDuracao());
				servico.setId(sv.getId());
				servico.setNome(sv.getNome());
				servico.setPreco(sv.getPreco());
				servicos.add(servico);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return servicos;
	}
	
	@Path("listarBancos")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Banco> listarBancos() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Banco> bancos = new ArrayList<br.com.salao.ws.xml.modelo.Banco>();
		try {
			BancoServico bancoServico = (BancoServico) ResourceManagedBean.getServico("bancoServico");
			List<Banco> bancs = bancoServico.listar(true);
			for(Banco bn : bancs){
				br.com.salao.ws.xml.modelo.Banco banco = new br.com.salao.ws.xml.modelo.Banco();
				banco.setAtivo(bn.getAtivo());
				banco.setId(bn.getId());
				banco.setNome(bn.getNome());
				bancos.add(banco);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return bancos;
	}
	
	@Path("listarBandeiras")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Bandeira> listarBandeiras() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Bandeira> bandeiras = new ArrayList<br.com.salao.ws.xml.modelo.Bandeira>();
		try {
			BandeiraServico bandeiraServico = (BandeiraServico) ResourceManagedBean.getServico("bandeiraServico");
			List<Bandeira> bands = bandeiraServico.listar(true);
			for(Bandeira bn : bands){
				br.com.salao.ws.xml.modelo.Bandeira bandeira = new br.com.salao.ws.xml.modelo.Bandeira();
				bandeira.setAtivo(bn.getAtivo());
				bandeira.setId(bn.getId());
				bandeira.setNome(bn.getNome());
				bandeiras.add(bandeira);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return bandeiras;
	}
	
	@Path("listarUsuarios")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.Usuario> listarUsuarios() throws ServicoException{
		List<br.com.salao.ws.xml.modelo.Usuario> usuarios = new ArrayList<br.com.salao.ws.xml.modelo.Usuario>();
		try {
			UsuarioServico usuarioServico = (UsuarioServico) ResourceManagedBean.getServico("usuarioServico");
			List<Usuario> usus = usuarioServico.listar(true);
			for(Usuario us :usus){
				br.com.salao.ws.xml.modelo.Usuario usuario = new br.com.salao.ws.xml.modelo.Usuario();
				usuario.setAdmin(us.getAdmin());
				usuario.setAtivo(us.getAtivo());
				usuario.setId(us.getId());
				usuario.setNome(us.getNome());
				usuario.setNomeLogin(us.getNomeLogin());
				usuarios.add(usuario);
			}
		} catch (Exception e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return usuarios;
	}
	
	@Path("calcularVendas")
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	public List<br.com.salao.ws.xml.modelo.vo.ExtratoVO> calcularVendas(@QueryParam("dtInicio") long dtInicio, @QueryParam("dtFinal") long dtFinal) throws ServicoException{
		List<br.com.salao.ws.xml.modelo.vo.ExtratoVO> extratos = new ArrayList<br.com.salao.ws.xml.modelo.vo.ExtratoVO>();
		try {			
			Date dataInicio = new Date(dtInicio);
			Date dataFinal = new Date(dtFinal);
			FinanceiroServico financeiroServico = (FinanceiroServico) ResourceManagedBean.getServico("financeiroServico");
			List<Comanda> comandas = financeiroServico.buscarComandas(dataInicio, dataFinal);			
			Float totalValor = 0f;
			Float totalDesconto2 = 0f;			
			for(Comanda com : comandas){
				String nomeCliente = null;		
				int idCliente = 0;
				Float totalVenda = 0F;
				Float totalDesconto = 0F;				
				for(Item it : com.getItens()){
					if(nomeCliente == null){
						nomeCliente = it.getCliente().getNome();
						idCliente = it.getCliente().getId();
					}
					totalVenda += (it.getValor() * it.getQuantidade());
					totalDesconto += it.getDesconto();
					totalValor += (it.getValor() * it.getQuantidade());
					totalDesconto2 += it.getDesconto();					
				}
				br.com.salao.ws.xml.modelo.vo.ExtratoVO extrato = new br.com.salao.ws.xml.modelo.vo.ExtratoVO();
				extrato.setData(com.getDataPagamento());
				extrato.setCliente(nomeCliente);
				extrato.setIdCliente(idCliente);
				if(totalDesconto > 0){
					extrato.setDescontoTotalComanda(totalDesconto * -1);					
				}else{
					extrato.setDescontoTotalComanda(totalDesconto);
				}
				extrato.setTotal(totalVenda - totalDesconto);
				extrato.setValorTotalComanda(totalVenda);				
				extratos.add(extrato);							
			}
		} catch (ServicoException e) {
			logg.error("Web Service Erro", e);
			throw new ServicoException(e.getMessage());
		}
		return extratos;
	}

}
