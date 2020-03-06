package libManager;

import java.util.ArrayList;

public class User {

	private String username;
	private String password;
	private ArrayList<Book> borrowedBooks = new ArrayList<>();
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void addBorrowedBooks(Book newBook) {
		this.borrowedBooks.add(newBook);
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}


	

}
