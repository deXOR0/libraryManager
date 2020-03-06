package libManager;

import java.util.*;
import java.io.*;

public class Main {
	
	Scanner scan = new Scanner(System.in);
	Scanner fileScan;
	File userdata = new File("userdata.txt");
	File bookdata = new File("bookdata.txt");
	ArrayList<User> users = new ArrayList<>();
	ArrayList<Book> bookList = new ArrayList<>();
	ArrayList<Book> availableBook = new ArrayList<>();
	User currentUser;

	void clear() {
		for (int i = 0; i < 20; i++) {
			System.out.println();
		}
	}
	
	void loadUserData() {
		users.clear();
		try {
			fileScan = new Scanner(userdata);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (fileScan.hasNextLine()) {
			String temp = fileScan.nextLine();
			String[] splits = temp.split(",");
			User thisUser = new User(splits[0], splits[1]);
			if (splits.length > 2) {
				for (int i = 2; i < splits.length; i += 2) {
					Book thisBook = new Book(splits[i], splits[i + 1]);
					thisUser.addBorrowedBooks(thisBook);
				} 
			}
			users.add(thisUser);
		}
	}
	
	void loadBookData() {
		bookList.clear();
		try {
			fileScan = new Scanner(bookdata);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (fileScan.hasNextLine()) {
			String temp = fileScan.nextLine();
			String[] splits = temp.split(",");
			bookList.add(new Book(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), splits[2], splits[3]));
		}
	}
	
	void saveUserData() {
		Formatter x = null;
		try {
			x = new Formatter(userdata);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (User thisUser : users) {
			x.format("%s,%s", thisUser.getUsername(), thisUser.getPassword());
			for (Book thisBook : thisUser.getBorrowedBooks()) {
				x.format(",%s,%s", thisBook.getTitle(), thisBook.getAuthor());
			}
			x.format("\n");
		}
		x.close();
		loadUserData();
	}
	
	void saveBookData() {
		Formatter x = null;
		try {
			x = new Formatter(bookdata);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Book thisBook : bookList) {
			x.format("%d,%d,%s,%s\n", thisBook.getInitialQty(), thisBook.getQty(), thisBook.getTitle(), thisBook.getAuthor());
		}
		x.close();
		loadBookData();
	}
	
	void adminMenu() {
		int choose = -1;
		
		do {
			clear();
			System.out.println("Welcome, admin");
			System.out.println("==========================");
			System.out.println("1. View All Books");
			System.out.println("2. View Borrowed Books");
			System.out.println("3. View Available Books");
			System.out.println("4. Add a Book");
			System.out.println("5. Remove a Book");
			System.out.println("6. User Manager");
			System.out.println("7. Logout");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 1 || choose > 7);
		switch(choose) {
		case 1:
			viewAll();
			scan.nextLine();
			adminMenu();
			break;
		case 2:
			viewBorrowedAll();
			break;
		case 3:
			viewAvailable();
			scan.nextLine();
			adminMenu();
			break;
		case 4:
			addBook();
			break;
		case 5:
			removeBook();
			break;
		case 6:
			userManager();
			break;
		case 7:
			mainMenu();
			break;
		}
	}
	
	void viewBorrowedAll() {
		clear();
		boolean borrowed = false;
		for (User thisUser : users) {
			if (!thisUser.getBorrowedBooks().isEmpty()) {
				System.out.println("Username : " + thisUser.getUsername());
				borrowed = true;
				for (Book thisBook : thisUser.getBorrowedBooks()) {
					
					System.out.println("  Title : " + thisBook.getTitle());
					System.out.println("  Author : " + thisBook.getAuthor());
					System.out.println();
				}
			}
			System.out.println();
		}
		if (!borrowed) {
			clear();
			System.out.println("Nobody is currently borrowing any book!");
		}
		scan.nextLine();
		adminMenu();
	}
	
	void addBook() {
		String title, author;
		int initialQty;
		clear();
		System.out.print("Input Book Title [x to cancel] : ");
		title = scan.nextLine();
		if (title.equals("x")) {
			adminMenu();
		}
		System.out.print("Input Book Author [x to cancel] : ");
		author = scan.nextLine();
		if (author.equals("x")) {
			adminMenu();
		}
		System.out.print("Input Book Quantity [0 to cancel] : ");
		initialQty = scan.nextInt();
		scan.nextLine();
		if (initialQty == 0) {
			adminMenu();
		}
		boolean existed = false;
		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getTitle().equals(title) && bookList.get(i).getAuthor().equals(author)) {
				bookList.get(i).setInitialQty(bookList.get(i).getInitialQty() + initialQty);
				bookList.get(i).setQty(bookList.get(i).getQty() + initialQty);
				existed = true;
				break;
			}
		}
		if (!existed) {
			bookList.add(new Book(initialQty, title, author));
		}
		saveBookData();
		System.out.print("You have successfully added " + initialQty + " " + title + "(s) ");
		System.out.println("by " + author);
		scan.nextLine();
		adminMenu();
	}
	
	void removeBook() {
		int choose = -1;
		do {
			viewAll();
			System.out.println("You can only remove a book that isn't currently borrowed by someone");
			System.out.println("Which book do you wan't to remove? [0 to cancel]");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}while (choose < 0 || choose > availableBook.size());
		if (choose == 0) {
			adminMenu();
		}
		else {
			String confirm = "0";
			do {
				clear();
				System.out.println("You have selected :");
				System.out.println("Title : " + availableBook.get(choose-1).getTitle());
				System.out.println("Author : " + availableBook.get(choose-1).getAuthor());
				System.out.println("Quantity : " + availableBook.get(choose-1).getInitialQty());
				System.out.println("Are you sure you want to delete this? This action is irreversible!");
				System.out.print(">> ");
				confirm = scan.nextLine();
			}while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));
			if (confirm.equalsIgnoreCase("n")) {
				System.out.println("Action is cancelled");
				scan.nextLine();
				adminMenu();
			}
			else {
				clear();
				for (int i = 0; i < bookList.size(); i++) {
					if (availableBook.get(choose-1).equals(bookList.get(i))) {
						if (bookList.get(i).getQty() < bookList.get(i).getInitialQty()) {
							System.out.println("You cannot delete this book as it's currently being borrowed by someone!");
							scan.nextLine();
							removeBook();
						}
						else {
							bookList.remove(i);
							saveBookData();
							System.out.println("Book succesfully deleted!");
							scan.nextLine();
							removeBook();
						}
					}
				}
			}
		}
	}
	
	void userManager() {
		clear();
		if (users.size() > 1) {
			int count = 1;
			for (User thisUser : users) {
				if (thisUser.getUsername().equals("admin")) {
					continue;
				}
				System.out.println(count++ + ". " + thisUser.getUsername());
				System.out.println("Books currently borrowed : " + thisUser.getBorrowedBooks().size());
				System.out.println();
			} 
		}
		else {
			System.out.println("No registered user found!");
		}
		scan.nextLine();
		adminMenu();
	}
	
	void userMenu() {
		int choose = -1;
		do {
			clear();
			System.out.println("Welcome, " + currentUser.getUsername());
			System.out.println("======================");
			System.out.println("1. Borrow a Book");
			System.out.println("2. Return a Book");
			System.out.println("3. View All Books");
			System.out.println("4. Profile Settings");
			System.out.println("5. Logout");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 1 || choose > 5);
		switch (choose) {
		case 1:
			borrow();
			break;
		case 2:
			returnBook();
			break;
		case 3:
			viewAll();
			scan.nextLine();
			userMenu();
			break;
		case 4:
			settings();
			break;
		case 5:
			mainMenu();
			break;
		}
	}
	
	void settings() {
		int choose = -1;
		do {
			clear();
			System.out.println("Username : " + currentUser.getUsername());
			System.out.println("Books currently borrowed : " + currentUser.getBorrowedBooks().size());
			System.out.println();
			System.out.println("1. Change Password");
			System.out.println("2. Delete Account");
			System.out.println("3. User Menu");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 1 || choose > 3);
		switch (choose) {
		case 1:
			String password;
			do {
				clear();
				System.out.print("Input your old password [x to cancel] : ");
				password = scan.nextLine();
				if (password.equals("x")) {
					settings();
				}
			}while (!currentUser.getPassword().equals(password));
			boolean validPassword = false;
			do {
				System.out.print("Input Your Password [Alphanumeric | x to cancel] : ");
				password = scan.nextLine();
				if (password.equals("x")) {
					mainMenu();
				}
				else {
					boolean hasAlpha = false, hasDigit = false;
					for (int i = 0; i < password.length(); i++) {
						if (Character.isAlphabetic(password.charAt(i))) {
							hasAlpha = true;
						}
						else if (Character.isDigit(password.charAt(i))) {
							hasDigit = true;
						}
						if (hasAlpha && hasDigit) {
							validPassword = true;
							break;
						}
					}
				}
				if (password.equals(currentUser.getPassword())) {
					System.out.println("You cannot use your current password!");
					scan.nextLine();
					validPassword = false;
				}
			}while (!validPassword);
			currentUser.setPassword(password);
			saveUserData();
			System.out.println("You changed your password!");
			scan.nextLine();
			settings();
			break;
		case 2:
			String confirm = "0";
			do {
				clear();
				System.out.print("Input your password [x to cancel] : ");
				password = scan.nextLine();
				if (password.equals("x")) {
					settings();
				}
			}while (!currentUser.getPassword().equals(password));
			do {
				clear();
				System.out.println("Are you sure you want to delete your account?");
				System.out.println("This action is irreversible!");
				System.out.print("[Y/N] >> ");
				confirm = scan.nextLine();
			}while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));
			if (confirm.equalsIgnoreCase("n")) {
				System.out.println("Action cancelled");
				scan.nextLine();
				settings();
			}
			else {
				if (!currentUser.getBorrowedBooks().isEmpty()) {
					clear();
					System.out.println("You cannot delete your account as you still haven't return " + currentUser.getBorrowedBooks().size() + " book(s)");
					System.out.println("Please return all of your books before you delete your account!");
					scan.nextLine();
					settings();
				}
				else {
					for (int i = 0; i < users.size(); i++) {
						if (users.get(i).getUsername().equals(currentUser.getUsername())) {
							users.remove(i);
							System.out.println("Your account has been deleted");
							scan.nextLine();
							mainMenu();
						}
					}
				}
			}
			break;
		case 3:
			userMenu();
			break;
		}
		
	}
	
	void viewAll() {
		int count = 1;
		clear();
		if (bookList.isEmpty()) {
			System.out.println("There aren't any books!");
			scan.nextLine();
			if (currentUser.getUsername().equals("admin")) {
				adminMenu();
			}
			else {
				userMenu();
			}
		}
		else {
			for (Book thisBook : bookList) {
				System.out.println(count++ + ". " + thisBook.getTitle());
				System.out.println("Author : " + thisBook.getAuthor());
				System.out.println("Quantity : " + thisBook.getInitialQty());
				System.out.println("Available : " + thisBook.getQty());
				System.out.println();
			}
		}
	}
	
	void viewAvailable() {
		clear();
		availableBook.clear();
		int count = 1;
		for (Book thisBook : bookList) {
			if (thisBook.getQty() > 0) {
				System.out.println(count++ + ". " + thisBook.getTitle());
				System.out.println("Author : " + thisBook.getAuthor());
				System.out.println("Available : " + thisBook.getQty());
				System.out.println();
				availableBook.add(thisBook);
			}
		}
		if (availableBook.isEmpty()) {
			System.out.println("Sorry, there currently aren't any book you can borrow");
			scan.nextLine();
			if (currentUser.getUsername().equals("admin")) {
				adminMenu();
			}
			else {
				userMenu();
			}
		}
	}
	
	void viewBorrowed() {
		int count = 1;
		clear();
		for (Book thisBook : currentUser.getBorrowedBooks()) {
			System.out.println(count++ + ". " + thisBook.getTitle());
			System.out.println("Author : " + thisBook.getAuthor());
			System.out.println();
		}
	}
	
	void returnBook() {
		if (currentUser.getBorrowedBooks().isEmpty()) {
			clear();
			System.out.println("You haven't borrowed any books!");
			scan.nextLine();
			userMenu();
		}
		int choose = -1;
		do {
			clear();
			System.out.println("1. Return a book");
			System.out.println("2. Return all book");
			System.out.println("3. User Menu");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 1 || choose > 3);
		switch (choose) {
		case 1:
			choose = -1;
			do {
				viewBorrowed();
				System.out.print("Select which book you want to return [0 to cancel] : ");
				try {
					choose = scan.nextInt();
				} catch (Exception e) {
					// TODO: handle exception
				}
				scan.nextLine();
			}while (choose < 0 || choose > currentUser.getBorrowedBooks().size());
			if (choose == 0) {
				returnBook();
			}
			else {
				Book temp = currentUser.getBorrowedBooks().get(choose-1);
				for (int i = 0; i < bookList.size(); i++) {
					if (bookList.get(i).getTitle().equals(temp.getTitle())) {
						if (bookList.get(i).getAuthor().equals(temp.getAuthor())) {
							bookList.get(i).setQty(bookList.get(i).getQty()+1);
							currentUser.getBorrowedBooks().remove(choose-1);
							break;
						}
					}
				}
				saveUserData();
				saveBookData();
				System.out.println("You have returned " + temp.getTitle());
				scan.nextLine();
				returnBook();
			}
			break;
		case 2:
			String confirm = "0";
			do {
				clear();
				System.out.println("You have " + currentUser.getBorrowedBooks().size() + " book(s) in your possesion");
				System.out.print("Are you sure you want to return them all? [Y/N] ");
				confirm = scan.nextLine();
			}while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));
			if (confirm.equalsIgnoreCase("n")) {
				System.out.println("Action cancelled");
				scan.nextLine();
				returnBook();
			}
			else {
				for (Book thisBook : currentUser.getBorrowedBooks()) {
					for (int i = 0; i < bookList.size(); i++) {
						if (bookList.get(i).getTitle().equals(thisBook.getTitle())) {
							if (bookList.get(i).getAuthor().equals(thisBook.getAuthor())) {
								bookList.get(i).setQty(bookList.get(i).getQty()+1);
								break;
							}
						}
					}
				}
				currentUser.getBorrowedBooks().clear();
				saveUserData();
				saveBookData();
				System.out.println("You have returned all the books you borrowed!");
				scan.nextLine();
				returnBook();
			}
			
			break;
		case 3:
			userMenu();
			break;
		}
	}
	
	void borrow() {
		int choose = -1;
		
		do {
			viewAvailable();
			System.out.print("Input the book you want to borrow [0 to cancel] : ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 0 || choose > availableBook.size());
		
		if (choose == 0) {
			mainMenu();
		}
		else {
			String confirm = "0";
			do {
				clear();
				System.out.println(availableBook.get(choose-1).getTitle());
				System.out.println(availableBook.get(choose-1).getAuthor());
				System.out.print("Are you sure you want to borrow this book? [Y/N] ");
				confirm = scan.nextLine();
			}while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));
			if (confirm.equalsIgnoreCase("n")) {
				borrow();
			}
			else {
				for (int i = 0; i < bookList.size(); i++) {
					if (bookList.get(i).getTitle().equals(availableBook.get(choose-1).getTitle())) {
						if (bookList.get(i).getAuthor().equals(availableBook.get(choose-1).getAuthor())) {
							bookList.get(i).setQty(bookList.get(i).getQty()-1);
							currentUser.addBorrowedBooks(bookList.get(i));
							saveUserData();
							saveBookData();
							System.out.println("Action Success!");
							System.out.println("Visit user menu to return the book");
							scan.nextLine();
							break;
						}
					}
				}
			}
		}
		userMenu();
	}
	
	void login() {
		clear();
		String username, password;
		boolean validUser = false, validPassword = false;
		do {
			System.out.print("Input Your Username [x to cancel] : ");
			username = scan.nextLine();
			if (username.equals("x")) {
				mainMenu();
			}
			else {
				for (User thisUser : users) {
					if (thisUser.getUsername().equals(username)) {
						validUser = true;
						currentUser = thisUser;
						break;
					}
				}
				if (!validUser) {
					System.out.println("Username not registered!");
				}
			}
		}while (!validUser);
		
		do {
			System.out.print("Input Your Password [x to cancel]: ");
			password = scan.nextLine();
			if (password.equals("x")) {
				mainMenu();
			}
			else if (currentUser.getPassword().equals(password)) {
				validPassword = true;
			}
			else {
				System.out.println("Password Mismatch!");
			}
		}while (!validPassword);
		System.out.println("Login Success!");
		scan.nextLine();
		if (currentUser.getUsername().equals("admin")) {
			adminMenu();
		}
		else {
			userMenu();
		}
	}
	
	void register() {
		clear();
		String username, password;
		boolean validUser = false, validPassword = false;

		do {
			System.out.print("Input Your Username [Must be unique | x to cancel] : ");
			username = scan.nextLine();
			if (username.equals("x")) {
				mainMenu();
			}
			else {
				validUser = true;
				for (User thisUser : users) {
					if (thisUser.getUsername().equals(username)) {
						System.out.println("Username already taken!");
						validUser = false;
						break;
					}
				}
			}
		}while (!validUser);
		
		do {
			System.out.print("Input Your Password [Alphanumeric | x to cancel] : ");
			password = scan.nextLine();
			if (password.equals("x")) {
				mainMenu();
			}
			else {
				boolean hasAlpha = false, hasDigit = false;
				for (int i = 0; i < password.length(); i++) {
					if (Character.isAlphabetic(password.charAt(i))) {
						hasAlpha = true;
					}
					else if (Character.isDigit(password.charAt(i))) {
						hasDigit = true;
					}
					if (hasAlpha && hasDigit) {
						validPassword = true;
						break;
					}
				}
			}
		}while (!validPassword);
		
		users.add(new User(username, password));
		saveUserData();
		System.out.println("Register Success!");
		scan.nextLine();
		mainMenu();
	}
	
	void mainMenu() {
		int choose = -1;
		do {
			clear();
			System.out.println("Library Manager");
			System.out.println("===============");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			System.out.print(">> ");
			try {
				choose = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			scan.nextLine();
		}while (choose < 1 || choose > 3);
		switch (choose) {
		case 1:
			login();
			break;
		case 2:
			register();
			break;
		case 3:
			System.exit(0);
			break;
		}
	}
	
	public Main() {
		loadUserData();
		loadBookData();
		mainMenu();
	}
	
	public static void main (String[] args) {
		new Main();
	}

}
