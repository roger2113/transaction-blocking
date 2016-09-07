package jee_example.transaction_block.service;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jee_example.transaction_block.service.domain.Book;

@Path("table")
public class AppResource {
	
	AppRepository appRepository;
	
	@Inject
	public void setAppRepository(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Integer hello(){
		return 42;
	}
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Book findCompanies(@PathParam("id") int id) {
		return appRepository.findBook(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addData(Book book){
		appRepository.updateBook(book);
	}

}
