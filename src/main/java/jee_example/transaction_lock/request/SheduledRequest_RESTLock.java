package jee_example.transaction_lock.request;

import java.util.Random;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jee_example.transaction_lock.service.domain.Book;

@Singleton
public class SheduledRequest_RESTLock {

	private static final Logger LOGGER = Logger.getLogger(SheduledRequest_DSLock.class.getSimpleName());

	public final static String BASE_URL = "http://localhost:8080/transactions/book/1";

	static Client client1;
	static Client client2;
	static Random random = new Random();
	
	private static Response getEntity(Client client) {
		LOGGER.info(String.format("Try to get entity. Sending request..."));
		Response response = client1.target(BASE_URL).
				request().
				accept(MediaType.APPLICATION_JSON_TYPE).
				get();
		return response;
	}

	private static Response updateItem(Client client, Entity entity, EntityTag etag) {
		LOGGER.info(String.format("Sending request for update..."));
		Response response = client.target(BASE_URL).			
				request().
				header("If-Match", etag).
				put(entity);
		if (response.getStatus() == 412){
			LOGGER.info(String.format("Response status = %s(%s); entity update failed.", 
					response.getStatus(), response.getStatusInfo()));
			return response;
		}
		LOGGER.info(String.format("Response status = %s(%s)", response.getStatus(), response.getStatusInfo()));
		LOGGER.info(String.format("Entity has been updated. Entity info: %s; response updated ETag: %s", 
									response.readEntity(entity.getEntity().getClass()), response.getEntityTag()));
		return response;
	}

	@Schedule(hour = "*", minute = "*", second = "30")
	public void emulateLocking() {
		
		LOGGER.info("Starting... Initializing clients...");
		client1 = ClientBuilder.newClient();
		client2 = ClientBuilder.newClient();
		
		Response response = getEntity(client1);
		Book book = response.readEntity(Book.class);
		EntityTag etag = response.getEntityTag();
		response.close();
		
		LOGGER.info(String.format("Got entity: %s; with ETag: %s", book, etag));
			
		book.setPrice(random.nextInt(40));
		
		LOGGER.info(String.format("Try to update entity from client_2: %s; ETag = '%s' ", book, etag));
		Response response2 = updateItem(client2, Entity.json(book), etag);
		response2.close(); 
		
		book.setPrice(random.nextInt(40));
		LOGGER.info(String.format("Try to update entity from client_1: %s; ETag = '%s' ", book, etag));
		Response response1 = updateItem(client1, Entity.json(book), etag);
		response1.close();
		
		LOGGER.info("Closing clients...");
		client1.close();
		client2.close();
	}

}

