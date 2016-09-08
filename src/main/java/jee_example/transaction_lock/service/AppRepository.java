package jee_example.transaction_lock.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import jee_example.transaction_lock.service.domain.Book;

@Stateless
public class AppRepository {

	public static final Logger LOGGER = Logger.getLogger(AppRepository.class.getName());

	@PersistenceContext(unitName = "book-unit")
	private EntityManager entityManager;

	public Book addBook(Book book) {
		entityManager.persist(book);
		return book;
	}

	public Optional<Book> findBook(int id) {
		return Optional.ofNullable(entityManager.find(Book.class, id));
	}

	public List<Book> findAll() {
		return entityManager.createNativeQuery("SELECT * FROM books").getResultList();
	}

	public Book updateBook(Book book) {
		Objects.requireNonNull(book);
		LOGGER.info(String.format("Book from request '%s'", book));
		Book updatedBook = entityManager.find(Book.class, book.getId());
		LOGGER.info(String.format("Book from datasource '%s'", updatedBook));

		/*
		if (book.getVersion() < updatedBook.getVersion()) {
			throw new OptimisticLockException(
					"Cannot update entity, beacause it has been already modified by another user");
		}*/

		try {
			entityManager.merge(updatedBook.updateFrom(book));
			entityManager.flush();
		} catch (OptimisticLockException e) {
			LOGGER.info("Cannot update entity, beacause it has been already modified by another user");
		}
		return updatedBook;
	}

	public void deleteBook(int id) {
		entityManager.remove(entityManager.find(Book.class, id));
	}

}
