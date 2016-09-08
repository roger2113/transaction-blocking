package jee_example.transaction_lock.service;

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

import jee_example.transaction_lock.request.SheduledRequest_DSLock;
import jee_example.transaction_lock.service.domain.Book;

@Singleton
public class SheduledRequest_RESTLock {

	private static final Logger LOGGER = Logger.getLogger(SheduledRequest_DSLock.class.getName());

	public final static String BASE_URL = "http://localhost:8080/transactions/book/1";

	static Client client1;
	static Client client2;
	static Random random = new Random();
	
	private static Book getEntity(Client client) {
		LOGGER.info(String.format("Try to get entity. Sending request..."));
		Book response = client1.target(BASE_URL).
				request().
				accept(MediaType.APPLICATION_JSON_TYPE).
				get(Book.class);
		
		return response;
	}

	private static Object updateItem(Client client, Entity entity) {
		LOGGER.info(String.format("Sending request for update..."));
		Book response = client.target(BASE_URL).
				request().
				put(entity, Book.class);
		LOGGER.info(String.format("Entity has been updated. Entity info: '%s", response));
		return response;
	}

	@Schedule(hour = "*", minute = "*")
	public void emulateLocking() {
		
		LOGGER.info("Starting... Initializing clients...");
		client1 = ClientBuilder.newClient();
		//client2 = ClientBuilder.newClient();
		
		Book response = getEntity(client1);
		//EntityTag etag = response.
		//Book book = (Book)response.getEntity();
		//LOGGER.info("Got entity " + book.getClass().getSimpleName() + ":  " + book);
		LOGGER.info("Try to update entity from client_1: " + response);
		/*book.setPrice(random.nextInt(40));
		
		LOGGER.info("Try to update entity from client_2: " + book.toString());
		updateItem(client2, Entity.json(book));
			
		LOGGER.info("Try to update entity from client_1: " + book.toString());
		updateItem(client1, Entity.json(book));*/
		
		LOGGER.info("Closing clients...");
		client1.close();
		//client2.close();
	}

}

