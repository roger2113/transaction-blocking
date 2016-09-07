package jee_example.transaction_block.service.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "books")
public class Book {
	
    @Id
    @GeneratedValue
	private int id;
    
    @Column(name = "title")
	private String title;
	
    @Column(name = "quantity")
	private int quantity;
    
    @Version
    @Column(name = "updated_at")
    private Timestamp updatedAt;
	
	public Book(){	
	}
	
	public Book(int id, String title, int quantity) {
		this.id = id;
		this.title = title;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
