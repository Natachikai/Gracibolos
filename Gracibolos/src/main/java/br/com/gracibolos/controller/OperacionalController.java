package br.com.gracibolos.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.gracibolos.jdbc.dao.CaixaDao;
import br.com.gracibolos.jdbc.dao.CidadeDao;
import br.com.gracibolos.jdbc.dao.ClienteDao;
import br.com.gracibolos.jdbc.dao.ColaboradorDao;
import br.com.gracibolos.jdbc.dao.EncomendaDao;
import br.com.gracibolos.jdbc.dao.EstadoDao;
import br.com.gracibolos.jdbc.dao.FornecedorDao;
import br.com.gracibolos.jdbc.dao.MateriaPrimaDao;
import br.com.gracibolos.jdbc.dao.ProdutoDao;
import br.com.gracibolos.jdbc.model.Caixa;
import br.com.gracibolos.jdbc.model.Cidade;
import br.com.gracibolos.jdbc.model.Cliente;
import br.com.gracibolos.jdbc.model.Colaborador;
import br.com.gracibolos.jdbc.model.Encomenda;
import br.com.gracibolos.jdbc.model.Estado;
import br.com.gracibolos.jdbc.model.Fornecedor;
import br.com.gracibolos.jdbc.model.ItemEncomenda;
import br.com.gracibolos.jdbc.model.MateriaPrima;
import br.com.gracibolos.jdbc.model.Produto;
import br.com.gracibolos.jdbc.model.Status;

@Controller
public class OperacionalController {	
	/*
	 * 
	 * ###################### PRODUTOS ######################
	 * 
	 * */
	
	//PRODUTOS
	//mapeamento da jsp admin produtos
	@RequestMapping("/operacional-produtos")
	public ModelAndView produtos(){
		System.out.println("Entrou na pagina de listagem de produtos");
		//retorna a p�gina produtos
		return new ModelAndView("operacional/produtos");
	}
	
	//INCLUIR NOVO PRODUTO
	@RequestMapping("/operacional-incluir-produto")
	public ModelAndView incluir_produto(Produto produto, @RequestParam("file") MultipartFile file, HttpServletRequest request){
		System.out.println("Entrou na servlet de inclus�o de um novo produto");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia DAO do produto
	    ProdutoDao produtoDao = new ProdutoDao();

	    
	    if(!file.isEmpty()) {
		    produto.setFoto(file.getOriginalFilename().toString());
		    
		    try {
		    	
		    	//Pasta de destino
		        String Path = request.getServletContext().getRealPath("\\resources\\img\\produtos");
		        System.out.println("uploadRootPath=" + Path);
		 
		        File diretorio = new File(Path);
		        
		        //Verifica se o diret�rio j� existe, sen�o cria o diretorio
		        if (!diretorio.exists()) {
		        	diretorio.mkdirs();
		        }
		        
		        File serverFile = new File(diretorio.getAbsolutePath() + File.separator + file.getOriginalFilename());
		    	
		    	BufferedOutputStream stream = null;
		    	stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				FileCopyUtils.copy(file.getInputStream(), stream);
				
				stream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
	    }
	    
		try {
			//se o m�todo inserir passando um produto, for executado corretamente, status recebe verdadeiro
			if(produtoDao.inserir(produto)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/produtos");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
		//retorna o mv
		return mv;
	}
	
	//AlTERAR PRODUTO
	@RequestMapping("/operacional-alterar-produto")
	public ModelAndView alterar_produto(Produto produto, @RequestParam("file") MultipartFile file, HttpServletRequest request){
				
		System.out.println("Entrou na pagina de altera��o de produto");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do produto
		ProdutoDao produtoDao = new ProdutoDao();
	    
		if(!file.isEmpty()) {
		    produto.setFoto(file.getOriginalFilename().toString());
		    
		    try {
		    	
		    	//Pasta de destino
		        String Path = request.getServletContext().getRealPath("\\resources\\img\\produtos");
		        System.out.println("uploadRootPath=" + Path);
		 
		        File diretorio = new File(Path);
		        
		        //Verifica se o diret�rio j� existe, sen�o cria o diretorio
		        if (!diretorio.exists()) {
		        	diretorio.mkdirs();
		        }
		        
		        File serverFile = new File(diretorio.getAbsolutePath() + File.separator + file.getOriginalFilename());
		    	
		    	BufferedOutputStream stream = null;
		    	stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				FileCopyUtils.copy(file.getInputStream(), stream);

				stream.close();
			} catch (IOException e) {
				System.out.println("N�o foi poss�vel fazer o upload da imagem.");
				e.printStackTrace();
			}
		    
	    } else {
	    	
	    	if(produto.getFoto() == null){
		    	produto.setFoto("model.png");
	    	}
	    	
	    }
				
		try {
			//se o m�todo alterar passando um produto, for executado corretamente, status recebe verdadeiro
			if(produtoDao.alterar(produto)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView		
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/produtos");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
		//retorna o mv
		return mv;
	}
	
	//EXCLUIR PRODUTO
	@RequestMapping("/operacional-remover-produto")
	public ModelAndView excluir_produto(Produto produto){
		System.out.println("Entrou na pagina de exclus�o de produto");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do produto
		ProdutoDao produtoDao = new ProdutoDao();
		
		try {
			//se o m�todo excluir passando um produto, for executado corretamente, status recebe verdadeiro
			if(produtoDao.excluir(produto)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/produtos");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);
		//retorna o mv
		return mv;
	}	
	
	//PESQUISAR PRODUTOS
	@RequestMapping("/operacional-pesquisar-produto")
	public ModelAndView pesquisar_produto(String pesquisa){
		System.out.println("Realizou a pesquisa de produto");
		
		//cria uma nova inst�ncia DAO do produto
		ProdutoDao produtoDao = new ProdutoDao();
				
		List<Produto> produtos = null;
		
		try {
			produtos = produtoDao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/produtos");
		//passa a lista de produtos para a Expression Language chamada produtos
		mv.addObject("produtos", produtos);
		//retorna o mv	    
	    return mv;
	}
	
	//LISTAR PRODUTOS
		@RequestMapping("/operacional-listar-produtos")
		public ModelAndView listar_produtos(){
			System.out.println("Realizou a listagem de produtos");
			
			//cria uma nova inst�ncia DAO do produto
			ProdutoDao produtoDao = new ProdutoDao();
			
			List<Produto> produtos = null;
			
			try {
				//Guarda a lista de produtos num List
				produtos = produtoDao.listar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
			//inst�ncia uma nova modelView
			ModelAndView mv = new ModelAndView();
			//seta o caminho e o nome da jsp
			mv.setViewName("operacional/produtos");
			//passa a lista de produtos para a Expression Language chamada produtos
			mv.addObject("produtos", produtos);
			//retorna o mv	    
		    return mv;
		}
				
	/*
	 * 
	 * ###################### FORNECEDORES ######################
	 * 
	 * */
	
	//FORNECEDORES
	@RequestMapping("/operacional-fornecedores")
	public ModelAndView fornecedores(){
		System.out.println("Entrou na servlet de listagem de fornecedores");
		
		//cria uma nova inst�ncia DAO do estado
		EstadoDao estadoDao = new EstadoDao();
		//Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
		
	    //inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
	    mv.setViewName("operacional/fornecedores");
	    //passa a lista de estados para a Expression Language chamada estados
	    mv.addObject("estados", estados);
	    //retorna o mv
		return mv;
	}
	
	//INCLUIR NOVO FORNECEDOR
	@RequestMapping("/operacional-incluir-fornecedor")
	public ModelAndView incluir_fornecedor(Fornecedor fornecedor){
		System.out.println("Entrou na servlet de inclus�o de um novo fornecedor");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do fornecedor
		FornecedorDao fornecedorDao = new FornecedorDao();
		
		try {
			//se o m�todo inserir passando um fornecedor, for executado corretamente, status recebe verdadeiro
			if(fornecedorDao.inserir(fornecedor)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
     	//seta o caminho e o nome da jsp
		mv.setViewName("operacional/fornecedores");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
		//passa a lista de estados para a Expression Language chamada estados
		mv.addObject("estados", estados);
		//retorna o mv
		return mv;
	}
	
	//AlTERAR FORNECEDOR
	@RequestMapping("/operacional-alterar-fornecedor")
	public ModelAndView alterar_fornecedor(Fornecedor fornecedor){
		System.out.println("Entrou na pagina de altera��o de fornecedor");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do fornecedor
		FornecedorDao fornecedorDao = new FornecedorDao();
		
		try {
			//se o m�todo alterar passando um fornecedor, for executado corretamente, status recebe verdadeiro
			if(fornecedorDao.alterar(fornecedor)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/fornecedores");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
		//passa a lista de estados para a Expression Language chamada estados
	    mv.addObject("estados", estados);		
	    //retorna o mv
		return mv;
	}
	
	//EXCLUIR FORNECEDOR
	@RequestMapping("/operacional-remover-fornecedor")
	public ModelAndView excluir_fornecedor(Fornecedor fornecedor){
		System.out.println("Entrou na pagina de exclus�o de fornecedor");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do fornecedor
		FornecedorDao fornecedorDao = new FornecedorDao();
		
		try {
			//se o m�todo excluir passando um fornecedor, for executado corretamente, status recebe verdadeiro
			if(fornecedorDao.excluir(fornecedor)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/fornecedores");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);
		//passa a lista de estados para a Expression Language chamada estados
	    mv.addObject("estados", estados);		
	    //retorna o mv
		return mv;
	}
	
	//PESQUISAR FORNECEDORES
	@RequestMapping("/operacional-pesquisar-fornecedor")
	public ModelAndView pesquisar_fornecedor(String pesquisa){
		System.out.println("Realizou a pesquisa de fornecedor");
		
		//cria uma nova inst�ncia DAO do fornecedor
		FornecedorDao fornecedorDao = new FornecedorDao();
		List<Fornecedor> fornecedores = null;
		
		try {
			fornecedores = fornecedorDao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //inst�ncia uma nova modelView
	    ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
	    mv.setViewName("operacional/fornecedores");
	    //passa a lista de fornecedores para a Expression Language chamada fornecedores
	    mv.addObject("fornecedores", fornecedores);
		//passa a lista de estados para a Expression Language chamada estados
	    mv.addObject("estados", estados);	    
	    //retorna o mv
	    return mv;
	}
	
	//LISTAR FORNECEDORES
		@RequestMapping("/operacional-listar-fornecedores")
		public ModelAndView listar_fornecedores(){
			System.out.println("Realizou a listagem de fornecedores");
			
			//cria uma nova inst�ncia DAO do fornecedor
			FornecedorDao fornecedorDao = new FornecedorDao();
			List<Fornecedor> fornecedores = null;
			
			try {
				//Guarda a lista de fornecedores num List
				fornecedores = fornecedorDao.listar();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			//cria uma nova inst�ncia DAO do estado
		    EstadoDao estadoDao = new EstadoDao();
		    //Guarda a lista de estados num List
		    List<Estado> estados = estadoDao.listar_estados();
		    
		    //inst�ncia uma nova modelView
		    ModelAndView mv = new ModelAndView();
			//seta o caminho e o nome da jsp
		    mv.setViewName("operacional/fornecedores");
			//passa a lista de fornecedores para a Expression Language chamada fornecedores
		    mv.addObject("fornecedores", fornecedores);
			//passa a lista de estados para a Expression Language chamada estados	
		    mv.addObject("estados", estados);
			//retorna o mv		    
		    return mv;
		}
	
	/*
	 * 
	 * ###################### CLIENTES ######################
	 * 
	 * */
	
	//CLIENTES
	@RequestMapping("/operacional-clientes")
	public ModelAndView clientes(){
		System.out.println("Entrou na servlet de listagem de clientes");
		
		//cria uma nova inst�ncia DAO do estado
		EstadoDao estadoDao = new EstadoDao();
		//Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
		
	    //inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
	    mv.setViewName("operacional/clientes");
	    //passa a lista de estados para a Expression Language chamada estados	
	    mv.addObject("estados", estados);
	    //retorna o mv	
		return mv;
	}
	
	//INCLUIR NOVO CLIENTE
	@RequestMapping("/operacional-incluir-cliente")
	public ModelAndView incluir_cliente(Cliente cliente){
		System.out.println("Entrou na servlet de inclus�o de um novo cliente");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do cliente
		ClienteDao dao = new ClienteDao();
		
		try {
			//se o m�todo inserir passando um cliente, for executado corretamente, status recebe verdadeiro
			if(dao.inserir(cliente)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/clientes");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
		//passa a lista de estados para a Expression Language chamada estados	
		mv.addObject("estados", estados);
	    //retorna o mv
		return mv;
	}
	
	//AlTERAR CLIENTE
	@RequestMapping("/operacional-alterar-cliente")
	public ModelAndView alterar_cliente(Cliente cliente){
		System.out.println("Entrou na pagina de altera��o de cliente");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	  //cria uma nova inst�ncia DAO do cliente
		ClienteDao clienteDao = new ClienteDao();
		
		try {
			//se o m�todo alterar passando um cliente, for executado corretamente, status recebe verdadeiro
			if(clienteDao.alterar(cliente)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/clientes");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
		//passa a lista de estados para a Expression Language chamada estados	
		mv.addObject("estados", estados);
	    //retorna o mv		
		return mv;
	}
	
	//EXCLUIR CLIENTE
	@RequestMapping("/operacional-remover-cliente")
	public ModelAndView excluir_cliente(Cliente cliente){
		System.out.println("Entrou na pagina de exclus�o de cliente");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do cliente
		ClienteDao clienteDao = new ClienteDao();
		
		try {
			//se o m�todo excluir passando um cliente, for executado corretamente, status recebe verdadeiro
			if(clienteDao.excluir(cliente)){
				status = true;
			}
			//caso contr�rio, status recebe falso
			else{
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/clientes");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);
		//passa a lista de estados para a Expression Language chamada estados	
		mv.addObject("estados", estados);
		//retorna o mv		
		return mv;
	}
	
	//PESQUISAR CLIENTE
	@RequestMapping("/operacional-pesquisar-cliente")
	public ModelAndView pesquisar_cliente(String pesquisa){
		System.out.println("Realizou a pesquisa de cliente");
		
		//cria uma nova inst�ncia DAO do cliente
		ClienteDao clienteDao = new ClienteDao();
		List<Cliente> clientes = null;
		try {
			clientes = clienteDao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de clientes num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //inst�ncia uma nova modelView
	    ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
	    mv.setViewName("operacional/clientes");
	    //passa a lista de clientes para a Expression Language chamada clientes	
	    mv.addObject("clientes", clientes);
	    //passa a lista de estados para a Expression Language chamada estados	
	    mv.addObject("estados", estados);
	    //retorna mv
	    return mv;
	}

	//LISTAR CLIENTES
		@RequestMapping("/operacional-listar-clientes")
		public ModelAndView listar_cliente(){
			System.out.println("Realizou a listagem de clientes");
			
			//cria uma nova inst�ncia DAO do cliente
			ClienteDao clienteDao = new ClienteDao();
			List<Cliente> clientes = null;
			try {
				//Guarda a lista de clientes num List
				clientes = clienteDao.listar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//cria uma nova inst�ncia DAO do estado
		    EstadoDao estadoDao = new EstadoDao();
		    //Guarda a lista de estados num List
		    List<Estado> estados = estadoDao.listar_estados();
		    
		    //inst�ncia uma nova modelView
		    ModelAndView mv = new ModelAndView();
		    //seta o caminho e o nome da jsp
		    mv.setViewName("operacional/clientes");
		    //passa a lista de clientes para a Expression Language chamada clientes	
		    mv.addObject("clientes", clientes);
		    //passa a lista de estados para a Expression Language chamada estados	
		    mv.addObject("estados", estados);
		    //retorna mv
		    return mv;
		}

	/*
	 * 
	 * ###################### COLABORADOR ######################
	 * 
	 * */
	
	//COLABORADOR
	@RequestMapping("/operacional-colaboradores")
	public ModelAndView colaboradores(){
		System.out.println("Entrou na servlet de listagem de colaboradores");
		
		//cria uma nova inst�ncia DAO do estado
		EstadoDao estadoDao = new EstadoDao();
		//Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
		
	    //inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
	    mv.setViewName("operacional/colaboradores");
	    //passa a lista de estados para a Expression Language chamada estados
	    mv.addObject("estados", estados);
	    //retorna mv		
		return mv;
	}
	
	//INCLUIR NOVO COLABORADOR
	@RequestMapping("/operacional-incluir-colaborador")
	public ModelAndView incluir_colaborador(Colaborador colaborador){
		System.out.println("Entrou na servlet de inclus�o de um novo colaborador");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do colaborador
		ColaboradorDao dao = new ColaboradorDao();
		
		//se o m�todo inserir passando um colaborador, for executado corretamente, status recebe verdadeiro
		if(dao.inserir(colaborador)){
			status = true;
		}
		//caso contr�rio, status recebe falso
		else{
			status = false;
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/colaboradores");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
		//passa a lista de estados para a Expression Language chamada estados
		mv.addObject("estados", estados);
		//retorna mv
		return mv;
	}	
	
	//AlTERAR COLABORADOR
	@RequestMapping("/operacional-alterar-colaborador")
	public ModelAndView alterar_colaborador(Colaborador colaborador){
		System.out.println("Entrou na pagina de altera��o de colaborador");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //cria uma nova inst�ncia DAO do colaborador
		ColaboradorDao colaboradorDao = new ColaboradorDao();
		
		//se o m�todo alterar passando um colaborador, for executado corretamente, status recebe verdadeiro
		if(colaboradorDao.alterar(colaborador)){
			status = true;
		}
		//caso contr�rio, status recebe falso
		else{
			status = false;
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/colaboradores");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
		//passa a lista de clientes para a Expression Language chamada clientes
	    mv.addObject("estados", estados);
	    //retorna mv		
		return mv;
	}
	
	
	//EXCLUIR COLABORADOR
	@RequestMapping("/operacional-remover-colaborador")
	public ModelAndView excluir_colaborador(Colaborador colaborador){
		System.out.println("Entrou na pagina de exclus�o de colaborador");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	  //cria uma nova inst�ncia DAO do colaborador
		ColaboradorDao colaboradorDao = new ColaboradorDao();
		
		//se o m�todo excluir passando um colaborador, for executado corretamente, status recebe verdadeiro
		if(colaboradorDao.excluir(colaborador)){
			status = true;
		}
		//caso contr�rio, status recebe falso
		else{
			status = false;
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/colaboradores");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);
	    //passa a lista de clientes para a Expression Language chamada clientes
		mv.addObject("estados", estados);
	    //retorna mv		
		return mv;
	}
	
	//PESQUISAR COLABORADOR
	@RequestMapping("/operacional-procurar-colaborador")
	public ModelAndView pesquisar_colaborador(String pesquisa){
		System.out.println("Realizou a pesquisa de colaboradores");
		
		//cria uma nova inst�ncia DAO do colaborador
		ColaboradorDao dao = new ColaboradorDao();
		List<Colaborador> colaboradores = dao.pesquisar(pesquisa);
		
		//cria uma nova inst�ncia DAO do estado
	    EstadoDao estadoDao = new EstadoDao();
	    //Guarda a lista de estados num List
	    List<Estado> estados = estadoDao.listar_estados();
	    
	    //inst�ncia uma nova modelView
	    ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
	    mv.setViewName("operacional/colaboradores");
		//passa a lista de colaborador para a Expression Language chamada colaboradores
	    mv.addObject("colaboradores", colaboradores);
	    //passa a lista de clientes para a Expression Language chamada clientes
	    mv.addObject("estados", estados);
	    //retorna mv    
	    return mv;
	}
	
	//LISTAR COLABORADOR
		@RequestMapping("/operacional-listar-colaboradores")
		public ModelAndView listar_colaborador(){
			System.out.println("Realizou a listagem de colaboradores");
			
			//cria uma nova inst�ncia DAO do colaborador
			ColaboradorDao colaboradorDao = new ColaboradorDao();
			//Guarda a lista de colaborador num List
			List<Colaborador> colaboradores = colaboradorDao.listar();
			
			//cria uma nova inst�ncia DAO do estado
		    EstadoDao estadoDao = new EstadoDao();
		    //Guarda a lista de estados num List
		    List<Estado> estados = estadoDao.listar_estados();
		    
		    //inst�ncia uma nova modelView
		    ModelAndView mv = new ModelAndView();
		    //seta o caminho e o nome da jsp
		    mv.setViewName("operacional/colaboradores");
			//passa a lista de colaborador para a Expression Language chamada colaboradores
		    mv.addObject("colaboradores", colaboradores);
		    //passa a lista de clientes para a Expression Language chamada clientes
		    mv.addObject("estados", estados);
		    //retorna mv    
		    return mv;
		}

	/*
	 * 
	 * ###################### CIDADES ######################
	 * 
	 * */
	
	@RequestMapping(value="/operacional-pesquisar-cidade", method=RequestMethod.POST)
	public @ResponseBody List<Cidade> pesquisar_cidade(int id){
				
		List<Cidade> cidades = new ArrayList<Cidade>();

		//cria uma nova inst�ncia DAO do estado
		CidadeDao dao = new CidadeDao();
		try {
			cidades = dao.pesquisar_cidades(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    return cidades;
		
	}
	
	/*
	 * 
	 * ###################### MAT�RIA PRIMA ######################
	 * 
	 * */
	
	//MAT�RIA PRIMA
	//mapeamento da jsp admin materias-primas
	@RequestMapping("/operacional-materiasprimas")
	public ModelAndView materiaPrima(){
		System.out.println("Entrou na servlet de listagem de mat�ria prima");
		
		//retorna a p�gina materias-primas
		return new ModelAndView("operacional/materiasprimas");
	}
		
	//INCLUIR NOVA MAT�RIA PRIMA
	@RequestMapping("/operacional-incluir-materiaprima")
	public ModelAndView incluir_materiaPrima(MateriaPrima materiaprima){
		System.out.println("Entrou na servlet de inclus�o de uma nova materiaPrima");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia dao da materia-prima
		MateriaPrimaDao dao = new MateriaPrimaDao();			
		
		try {
			//se o m�todo inserir passando uma materiaprima, for executado corretamente, status recebe verdadeiro
			if(dao.inserir(materiaprima)) {
				status = true;			
			}
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/materiasprimas");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
		//retorna o mv
		return mv;
	}
		
	//AlTERAR MAT�RIA PRIMA
	@RequestMapping("/operacional-alterar-materiaprima")
	public ModelAndView alterar_materiaPrima(MateriaPrima materiaprima){
		System.out.println("Entrou na pagina de altera��o de materiaPrima");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		
		//cria uma nova inst�ncia DAO da materia-prima
		MateriaPrimaDao materiaPrimaDao = new MateriaPrimaDao();			
			
		try
		{
			//se o m�todo alterar passando uma materiaprima, for executado corretamente, status recebe verdadeiro
			if(materiaPrimaDao.alterar(materiaprima)) {
				status = true;
			} else {
			//caso contr�rio, status recebe falso
				status = false;
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/materiasprimas");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
	    //retorna mv
		return mv;
			
	}
		
	//EXCLUIR MAT�RIA PRIMA
	@RequestMapping("/operacional-remover-materiaprima")
	public ModelAndView excluir_materiaPrima(MateriaPrima materiaprima){
		System.out.println("Entrou na pagina de exclus�o de materiaprima");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;	
		
		//cria uma nova inst�ncia dao da materia-prima
	    MateriaPrimaDao materiaPrimaDao = new MateriaPrimaDao();
			
		try {
			//se o m�todo excluir passando uma materiaprima, for executado corretamente, status recebe verdadeiro
			if(materiaPrimaDao.excluir(materiaprima)) {
				status = true;
			} 
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/materiasprimas");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);			
	    //retorna mv
		return mv;
	}
		
	//PESQUISAR MAT�RIA PRIMA
	@RequestMapping("/operacional-procurar-materiaprima")
	public ModelAndView pesquisar_materiaPrima(String pesquisa){
		System.out.println("Realizou a pesquisa de mat�ria prima");
		
		//cria uma nova inst�ncia dao da materia-prima
		MateriaPrimaDao dao = new MateriaPrimaDao();
		List<MateriaPrima> materiasprimas = null;
		try {
			materiasprimas = dao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/materiasprimas");
		//passa a lita de materia-prima para a Expression Language chamada materiasprimas
		mv.addObject("materiasprimas", materiasprimas);
	    //retorna mv		    
	    return mv;
	}
	
	//LISTAR MAT�RIA PRIMA
	@RequestMapping("/operacional-listar-materiasprimas")
	public ModelAndView listar_materiasprimas(){
		System.out.println("Realizou a listagem de mat�rias primas");
		
		//cria uma nova inst�ncia dao da materia-prima
		MateriaPrimaDao dao = new MateriaPrimaDao();
		List<MateriaPrima> materiasprimas = null;
		
		try {
			//Guarda a lista de materia-prima num List
			materiasprimas = dao.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/materiasprimas");
		//passa a lita de materia-prima para a Expression Language chamada materiasprimas
		mv.addObject("materiasprimas", materiasprimas);
	    //retorna mv		    
	    return mv;
	}
	
	/*
	 * 
	 * ###################### CAIXA ######################
	 * 
	 * */
	
	//CAIXA
	@RequestMapping("/operacional-caixa")
	public ModelAndView caixa(){
		System.out.println("Entrou na servlet de listagem de mat�ria prima");
						
		return new ModelAndView("operacional/caixa");
	}
	
	//INCLUIR NOVO CAIXA
	@RequestMapping("/operacional-incluir-caixa")
	public ModelAndView incluir_caixa(Caixa caixa){
		System.out.println("Entrou na servlet de inclus�o de um novo caixa");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		 
		//cria uma nova inst�ncia DAO do caixa
		CaixaDao dao = new CaixaDao();			
			
		try {
			//se o m�todo inserir passando um caixa, for executado corretamente, status recebe verdadeiro
			if(dao.inserir(caixa)) {
				status = true;
			} 
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/caixa");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status);
	    //retorna mv
		return mv;
	}
	
	//AlTERAR CAIXA
	@RequestMapping("/operacional-alterar-caixa")
	public ModelAndView alterar_caixa (Caixa caixa){
		System.out.println("Entrou na pagina de altera��o do caixa");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO do caixa
		CaixaDao dao = new CaixaDao();			
			
		try
		{
			//se o m�todo alterar passando um caixa, for executado corretamente, status recebe verdadeiro
			if(dao.alterar(caixa)) {
				status = true;
			}
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/caixa");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
	    //retorna mv
		return mv;
	}
			
	//EXCLUIR CAIXA
	@RequestMapping("/operacional-excluir-caixa")
	public ModelAndView excluir_caixa (Caixa caixa){
		System.out.println("Entrou na pagina de exclus�o do caixa");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;	
		//cria uma nova inst�ncia DAO do caixa		
		CaixaDao dao = new CaixaDao();			
				
		try {
			//se o m�todo excluir passando um caixa, for executado corretamente, status recebe verdadeiro
			if(dao.excluir(caixa)) {
				status = true;
			}
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/caixa");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);			
	    //retorna mv
		return mv;
	}
			
	//PESQUISAR CAIXA
	@RequestMapping("/operacional-pesquisar-caixa")
	public ModelAndView pesquisar_caixa (String pesquisa){
		System.out.println("Realizou a pesquisa do caixa");
		
		//cria uma nova inst�ncia DAO do caixa
		CaixaDao dao = new CaixaDao();		
		List<Caixa> caixas = null;
		try {
			caixas = dao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/caixa");
	    //passa a lista do caixa para a Expression Language chamada caixas
		mv.addObject("caixas", caixas);
	    //retorna mv
	    return mv;
	}
	
	/*
	 * 
	 * ###################### ENCOMENDA ######################
	 * 
	 * */
	
	//ENCOMENDA
	@RequestMapping("/operacional-encomendas")
	public ModelAndView encomenda(){
		System.out.println("Entrou na servlet de listagem de mat�ria prima");
			
			//TESTES
				List<ItemEncomenda> itens = new ArrayList<ItemEncomenda>();
		
				ItemEncomenda item = new ItemEncomenda();
				item.setId((long) 1);
				item.setNumero(1);
				item.setProdutoId(3l);
				item.setQuantidade(20);
				item.setValor(new BigDecimal("10.00"));
				item.setTotal(new BigDecimal("200.00"));
				
				ItemEncomenda item2 = new ItemEncomenda();
				item2.setId((long) 2);
				item2.setNumero(2);
				item2.setProdutoId(2l);
				item2.setQuantidade(20);
				item2.setValor(new BigDecimal("5.00"));
				item2.setTotal(new BigDecimal("100.00"));
				
				itens.add(item);
				itens.add(item2);
			//FIM DOS TESTES

		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
		//seta o caminho e o nome da jsp
		mv.setViewName("operacional/encomendas");
		//passa a lista de item para a Expression Language chamada itens
		mv.addObject("itens", itens);
		//retorna mv
		return mv;
	}
		
	//INCLUIR NOVA ENCOMENDA
	@RequestMapping("/operacional-incluir-encomenda")
	public ModelAndView incluir_encomenda(Encomenda encomenda){
		System.out.println("Entrou na servlet de inclus�o de uma nova materiaPrima");
		
		
		/*
		 
		 
		 
		 C�digo para ler os produtos de um input array html
		 
		 
		 
		 System.out.println(encomendas.getProdutos().size());
		
		for(int i=0; i<encomendas.getProdutos().size();i++){
			if(encomendas.getProdutos().get(i).getNome() != ""){
				
				Produto produto = new Produto();
				produto.setId(encomendas.getProdutos().get(i).getId());
				produto.setNome(encomendas.getProdutos().get(i).getNome());

				System.out.println(produto.getId());
				System.out.println(produto.getNome());
			}
		}
		 
		 
		 */
		
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		//boolean status = false;
		//cria uma nova inst�ncia DAO da encomenda
		//EncomendaDao dao = new EncomendaDao();			
		Status status = new Status();
		
		try {
			//se o m�todo inserir passando uma encomenda, for executado corretamente, status recebe verdadeiro
			//status = dao.inserir(encomenda);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/encomendas");
		//passa o retorno do status para a Expression Language chamada incluir
		mv.addObject("incluir", status.getStatus1());
		mv.addObject("incluirItens", status.getStatus2());
		mv.addObject("numeroEncomenda", status.getNumeroEncomenda());
	    //retorna mv
		return mv;
	}
		
	//AlTERAR ENCOMENDA
	@RequestMapping("/operacional-alterar-encomenda")
	public ModelAndView alterar_encomenda(Encomenda encomenda){
		System.out.println("Entrou na pagina de altera��o de materiaPrima");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;
		//cria uma nova inst�ncia DAO da encomenda
		EncomendaDao dao = new EncomendaDao();			
			
		try
		{
			//se o m�todo alterar passando uma encomenda, for executado corretamente, status recebe verdadeiro
			if(dao.alterar(encomenda)) {
				status = true;
			}
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/encomendas");
		//passa o retorno do status para a Expression Language chamada alterar
		mv.addObject("alterar", status);
	    //retorna mv			
		return mv;
	}
		
	//EXCLUIR ENCOMENDA
	@RequestMapping("/operacional-excluir-encomenda")
	public ModelAndView excluir_encomenda(Encomenda encomenda){
		System.out.println("Entrou na pagina de exclus�o de materiaprima");
		
		//reclara um status como falso, pra depois verificar se a condi��o foi atendida ou n�o.
		boolean status = false;	
		//cria uma nova inst�ncia DAO da encomenda	
		EncomendaDao dao = new EncomendaDao();
			
		try {
			//se o m�todo excluir passando uma encomenda, for executado corretamente, status recebe verdadeiro
			if(dao.excluir(encomenda)) {
				status = true;
			} 
			//caso contr�rio, status recebe falso
			else {
				status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/encomendas");
		//passa o retorno do status para a Expression Language chamada excluir
		mv.addObject("excluir", status);			
	    //retorna mv			
		return mv;
	}
		
	//PESQUISAR ENCOMENDA
	@RequestMapping("/operacional-pesquisar-encomenda")
	public ModelAndView pesquisar_encomenda(String pesquisa){
		System.out.println("Realizou a pesquisa de mat�ria prima");
		
		//cria uma nova inst�ncia DAO da encomenda
		EncomendaDao dao = new EncomendaDao();
		
		List<Encomenda> encomendas = null;
		try {
			encomendas = dao.pesquisar(pesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//inst�ncia uma nova modelView
		ModelAndView mv = new ModelAndView();
	    //seta o caminho e o nome da jsp
		mv.setViewName("operacional/encomendas");
	    //passa a lista de encomenda para a Expression Language chamada encomendas
		mv.addObject("encomendas", encomendas);
	    //retorna mv		    
	    return mv;
	}
	
}