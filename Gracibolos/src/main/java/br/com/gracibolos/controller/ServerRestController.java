package br.com.gracibolos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.gracibolos.jdbc.dao.CaixaDao;
import br.com.gracibolos.jdbc.dao.ClienteDao;
import br.com.gracibolos.jdbc.dao.EncomendaDao;
import br.com.gracibolos.jdbc.dao.FornecedorDao;
import br.com.gracibolos.jdbc.dao.ItemEncomendaDao;
import br.com.gracibolos.jdbc.dao.MateriaPrimaDao;
import br.com.gracibolos.jdbc.dao.ProdutoDao;
import br.com.gracibolos.jdbc.model.Caixa;
import br.com.gracibolos.jdbc.model.Cliente;
import br.com.gracibolos.jdbc.model.Encomenda;
import br.com.gracibolos.jdbc.model.Fornecedor;
import br.com.gracibolos.jdbc.model.ItemEncomenda;
import br.com.gracibolos.jdbc.model.MateriaPrima;
import br.com.gracibolos.jdbc.model.Produto;
//import br.com.gracibolos.jdbc.model.Status;

@RestController
public class ServerRestController {
	
	private Produto p;
	private ProdutoDao produtoDao;
	private ClienteDao clienteDao;
	private List<Produto> produtosList;
	private List<Cliente> clientesList;
	private MateriaPrimaDao mpDao;
	private List<MateriaPrima> mpList;
	private FornecedorDao fornDao;
	private List<Fornecedor> fornList;
	private ItemEncomendaDao ItemEncDao;
	private List<ItemEncomenda> listItemEnc;
	private CaixaDao caixaDao;
	private Caixa c;
	private EncomendaDao daoEnc;
	private Encomenda e;
	//private boolean status = false;
	private String msg="";
	
	// AJAX
	private void colocarAcesso(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	}
	
	// ---------------------FORNECEDOR---------------------------------------------------------
	
	@RequestMapping(value = "/rest-pesquisar-fornecedor/{pesquisa}", method = RequestMethod.GET)  
	public List<Fornecedor> pesquisar_fornecedor(@PathVariable String pesquisa
			, HttpServletResponse response) {
		fornList = null;
		fornDao = new FornecedorDao();
		try {
			fornList = fornDao.pesquisar(pesquisa);
		} catch (Exception e) {
			System.out.println("ERRO - rest fornecedor, pesquisa fornecedor.");
			e.printStackTrace();
		}    
		colocarAcesso(response);
		return fornList;  
	}
	
	// ---------------------MAT�RIA PRIMA---------------------------------------------------------
	
	@RequestMapping(value = "/rest-pesquisa-materiaprima/{pesquisa}", method = RequestMethod.GET)
	public List<MateriaPrima> pesquisar_materiprima(@PathVariable String pesquisa
			, HttpServletResponse response) 
	{
		mpList = null;
		mpDao = new MateriaPrimaDao();
		try {
			mpList = mpDao.pesquisar(pesquisa);
		} catch (Exception e) {
			System.out.println("ERRO - rest materiaprima, pesquisa materiaprima.");
			e.printStackTrace();
		} 
		// AJAX
		colocarAcesso(response);
	   
		return mpList;  
	}
	
	
	// ---------------------PRODUTO--------------------------------------------------------- 
	
	@RequestMapping(value = "/rest-produtos", method = RequestMethod.GET)
	public List<Produto> listAllProdutos(HttpServletResponse response) {
		try {
			produtoDao = new ProdutoDao();
			produtosList = produtoDao.listar();
		} catch (Exception e) {
			System.out.println("ERRO - rest produtos, lista de produtos.");
			e.printStackTrace();
		}
		colocarAcesso(response);
		
		return produtosList;
    }
	
	// OK
	@RequestMapping(value = "/rest-pesquisar-produto-id/{id}", method = RequestMethod.GET)
	 public Produto findById(@PathVariable int id, HttpServletResponse response) {  
		produtoDao = new ProdutoDao();
		p = new Produto();
		try {
			p = produtoDao.pesquisarPorId(id);
		} catch (Exception e) {
			System.out.println("ERRO - rest produtos, busca produtos por id.");
			e.printStackTrace();
		}    
		colocarAcesso(response);
        
        return p;  
	 }
	
	// OK
	@RequestMapping(value = "/rest-pesquisar-produto-nome/{pesquisa}", method = RequestMethod.GET) 
	public List<Produto> pesquisar_produto(@PathVariable String pesquisa, HttpServletResponse response) {
		produtoDao = null;
		produtoDao = new ProdutoDao();
		try {
			produtosList = produtoDao.pesquisar(pesquisa);
		} catch (Exception e) {
			System.out.println("ERRO - rest produtos, pesquisa produtos.");
			e.printStackTrace();
		}   
		colocarAcesso(response);
		
		return produtosList;  
	}

	// ---------------------CLIENTE---------------------------------------------------------
	
	@RequestMapping(value = "/rest-clientes/{nome}", method = RequestMethod.GET) 
	public List<Cliente> ListClientes(@PathVariable String nome, HttpServletResponse response){
		clienteDao = new ClienteDao();
		try {
			clientesList = clienteDao.pesquisar(nome);
		} catch (Exception e) {
			System.out.println("ERRO - rest clientes, lista de clientes.");
			e.printStackTrace();
		}
		colocarAcesso(response);
		
		return clientesList;
	}
	
	// ---------------------ITENS DA ENCOMENDA---------------------------------------------------------
	
	@RequestMapping(value = "/rest-itensencomenda/{encomenda}", method = RequestMethod.GET) 
	public List<ItemEncomenda> ListItensEncomenda(@PathVariable String encomenda
			, HttpServletResponse response){
		ItemEncDao = new ItemEncomendaDao();
		try {
			listItemEnc = ItemEncDao.pesquisar(encomenda);
		} catch (Exception e) {
			System.out.println("ERRO - rest itensencomenda, lista de itensencomenda.");
			e.printStackTrace();
		}
		colocarAcesso(response);
		
		return listItemEnc;
	}
	
	@RequestMapping(value = "/rest-itensencomenda/", method = RequestMethod.POST)
	public ResponseEntity<String> createItemEncomenda(@RequestBody List<ItemEncomenda> list
			,HttpSession session){
		
		ItemEncDao = new ItemEncomendaDao();
		for(ItemEncomenda ie : list){
			System.out.println(//"id : "+ie.getId() //mysql ignora
							"\nprodutoId : "+ie.getProdutoId()
							+"\nencomendaId "+ie.getEncomendaId()
							+"\nqtd "+ie.getQuantidade()
							+"\nvalor "+ie.getValor()
							+"\ntotal "+ie.getTotal()						
					);
		}
		try {
			int qtd = ItemEncDao.inserirList(list); //Inserir a lista de itens que retorna a quantidade de registros
			if(qtd != 0){
				System.out.println(qtd+" itens inseridos");
				msg = Integer.toString(qtd);//Convers�o de inteiro para String			
				session.setAttribute("respostaItem", "ok");
				session.setAttribute("mensagemItem", msg);
			}else{
				System.out.println("ERRO - rest itens inseridos.");
				msg = "erro";			
				session.setAttribute("respostaItem", msg);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	// ---------------------ENCOMEDA---------------------------------------------------------
	
	@RequestMapping(value = "/rest-encomenda-num/", method = RequestMethod.GET)//Preencher com o numero da encomenda
	public Long numeroEncomenda(){
		daoEnc = new EncomendaDao();		
		return daoEnc.numeroEncomenda();
	}
	
	@RequestMapping(value = "/rest-encomenda/{encomenda}", method = RequestMethod.GET)//Pesquisar
	public String pesquisarEncomenda(@PathVariable("encomenda") String encomenda
			, HttpServletResponse response){
		System.out.println("rest-pesquisar-encomenda : "+encomenda);
		daoEnc = new EncomendaDao();
		e = new Encomenda();
		msg="";
		try {
			if(daoEnc.pesquisarId(encomenda) == null){
				System.out.println("nulo");
				msg="nulo";
			}else{
				e = daoEnc.pesquisarId(encomenda);
				msg=Long.toString(e.getId());
				System.out.println("numero : "+msg);
			}
		} catch (Exception e) {
			System.out.println("ERRO - rest pesquisar encomenda\n");
			e.printStackTrace();
		}

		System.out.println(msg);
		return msg;
	}
	
	@RequestMapping(value = "/rest-encomenda/", method = RequestMethod.POST)// Inserir
	public ResponseEntity<String> createEncomenda(HttpSession session, 
			@RequestBody Encomenda encomenda){
		
		//session.setAttribute("respostaStatus", "ok");
		
		daoEnc = new EncomendaDao();
		System.out.println("N�mero : "+encomenda.getId()
						+"\nnomerazao : "+encomenda.getNomerazao()
						+"\nsattus "+encomenda.getStatus()
						+"\nDataencomenda "+encomenda.getDataencomenda()
						+"\nDataentrega "+encomenda.getDataentrega()
						+"\nDatacancelamento "+encomenda.getDatacancelamento()
						+"\nClienteid "+encomenda.getClienteid()
						+"\nResponsavel "+encomenda.getResponsavel()
						+"\nObs "+encomenda.getObs()
						+"\nTotal "+encomenda.getTotalprodutos()
						
				);
		
		try {
			if(daoEnc.inserir(encomenda)){//Encomenda
				System.out.println("Encomenda inicializada");
				msg = "ok";
				
				System.out.println("New session: " + session.getId());
				session.setAttribute("respostaInfo", msg);				
				session.setAttribute("mensagemInfo", "Encomenda inicializada");
				
			}else{
				System.out.println("ERRO - rest inserir encomenda.");
				msg = "erro";
				
				session.setAttribute("resposta", msg);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rest-encomenda/fat/", method = RequestMethod.PUT)//Alterar para faturado
	public ResponseEntity<String> updateEncomendaFat (@RequestBody String id, HttpSession session){
		
		System.out.println("Numero da encomenda : "+id+", alterado para faturado");
		daoEnc  = new EncomendaDao();
		try {
			if(daoEnc.alterarFaturado(id)){
				msg="ok";
				session.setAttribute("respostaFat", msg);
			}else{
				msg="erro";
				session.setAttribute("respostaFat", msg);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rest-encomenda/prod/", method = RequestMethod.PUT)//Alterar para produzindo
	public ResponseEntity<String> updateEncomendaProd (@RequestBody String id, HttpSession session){
		
		System.out.println("Numero da encomenda : "+id+", alterado para produzindo");
		daoEnc  = new EncomendaDao();
		try {
			if(daoEnc.alterarProduzindo(id)){
				msg="ok";
				session.setAttribute("respostaProd", msg);
			}else{
				msg="erro";
				session.setAttribute("respostaProd", msg);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rest-encomenda/fin/", method = RequestMethod.PUT)//Alterar para finalizado
	public ResponseEntity<String> updateEncomendaFin (@RequestBody String id, HttpSession session){
		
		System.out.println("Numero da encomenda : "+id+", alterado para finalizado");
		daoEnc  = new EncomendaDao();
		try {
			if(daoEnc.alterarFinalizado(id)){
				msg="ok";
				session.setAttribute("respostaFin", msg);
			}else{
				msg="erro";
				session.setAttribute("respostaFin", msg);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rest-encomenda/{id}", method = RequestMethod.DELETE)//Apagar
	public ResponseEntity<String> deleteEncomenda (@PathVariable String id){
		
		System.out.println("Numero da encomenda : "+id+", ser� deletado");
		
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	// ---------------------CAIXA---------------------------------------------------------
	
	@RequestMapping(value = "/rest-caixa/", method = RequestMethod.POST) 
	public ResponseEntity<String> createCaixa(@RequestBody Caixa caixa, HttpSession session){
		msg = "";	
		caixaDao = new CaixaDao();
		System.out.println("gastoRecebimento : "+caixa.getGastoRecebimento()
						+"\nencomendaId "+caixa.getEncomendaId()
						+"\nDataTransacao "+caixa.getDataTransacao()+" - insere no Dao"
						+"\nValor "+caixa.getValor()
						+"\nForma "+caixa.getForma()
						
				);
		try {
			if(caixaDao.inserir(caixa)){
				msg = "ok";
				session.setAttribute("respostaCaixa", msg);
			}else{
				msg = "erro";
				session.setAttribute("respostaCaixa", msg);
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rest-caixa/{caixa}", method = RequestMethod.GET) //Pesquisar
	public Caixa pesquisarcaixa(@PathVariable String caixa){
		caixaDao = new CaixaDao();
		c = new Caixa();
		try {
			if(caixaDao.pesquisar1(caixa) != null){
				c = caixaDao.pesquisar1(caixa);
				System.out.println("gastoRecebimento : "+c.getGastoRecebimento()
									+"\nencomendaId "+c.getEncomendaId()
									+"\nDataTransacao "+c.getDataTransacao()
									+"\nValor "+c.getValor()
									+"\nForma "+c.getForma()
									
							);
			}
		} catch (Exception e) {
			System.out.println("ERRO - rest pesquisar caixa\n"+e);
			e.printStackTrace();
		}
		
		return c;
	}
}
