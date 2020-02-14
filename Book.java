package main;

public class Book {

	private int qty;
	private int initialQty;
	private String title;
	private String author;
	
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Book(int initialQty, String title, String author) {
		super();
		this.initialQty = initialQty;
		this.qty = initialQty;
		this.title = title;
		this.author = author;
	}

	public int getInitialQty() {
		return initialQty;
	}
	
	public void setInitialQty(int initialQty) {
		this.initialQty = initialQty;
		int[] arr = {1,2,3,4,5,6,7,8,9,10};
		for (int i = 0; i < 10; i++) {
			if (arr[i] == 5) {
				System.out.println("Found!");
				break;
			}
		}
	}

	


	

}
