CREATE TABLE book (
    id 		     SERIAL PRIMARY KEY,	
    title        varchar(50) NOT NULL,
    price        int NOT NULL,
    updated_at   timestamp DEFAULT now(),        
    version      int DEFAULT 0          
);

INSERT INTO book (title, price) VALUES ('The Witcher', 29);
INSERT INTO book (title, price) VALUES ('The Misterious Island', 31);

