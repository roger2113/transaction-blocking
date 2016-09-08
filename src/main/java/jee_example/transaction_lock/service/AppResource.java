package jee_example.transaction_lock.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import jee_example.transaction_lock.service.domain.Book;

@Path("book")
public class AppResource {
	
	private AppRepository appRepository;
	
	@Inject
	public void setAppRepository(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Book> findAll(){
		return appRepository.findAll();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findBook(@PathParam("id") int id, @Context Request request) {
		Book book = appRepository.findBook(id).orElseThrow(() -> new NotFoundException("Book has not been found."));
		CacheControl cc = new CacheControl();
		cc.setMaxAge(86400);
		EntityTag etag = new EntityTag(Integer.toString(book.hashCode()));
		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if(builder != null) return builder.cacheControl(cc).tag(etag).build();
		return Response.ok(book).tag(etag).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBook(Book book){
		Book bookPersisted = appRepository.addBook(book);
		return Response.ok(bookPersisted).build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBook(@PathParam("id") int id,
			                   @Context Request request, Book bookToUpdate){
		Book book = appRepository.findBook(id).orElseThrow(() -> new NotFoundException("Book has not been found."));
		EntityTag etag = new EntityTag(Integer.toString(book.hashCode()));
		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if(builder != null) return builder.build();
		return Response.ok(appRepository.updateBook(bookToUpdate)).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteBook(@PathParam("id") int id) {
		appRepository.deleteBook(id);
		return Response.ok().build();
	}

}
