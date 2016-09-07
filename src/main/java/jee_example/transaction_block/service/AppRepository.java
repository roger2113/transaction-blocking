package jee_example.transaction_block.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jee_example.transaction_block.service.domain.Book;

@Stateless
public class AppRepository {
	
	public static final Logger LOGGER = Logger.getLogger(AppRepository.class.getName());
	
    @PersistenceContext(unitName = "book-unit")
    private EntityManager entityManager;
	
	public Book findBook(int id){		
		return entityManager.find(Book.class, id);
	}
	
	public void updateBook(Book book){

	}

}
