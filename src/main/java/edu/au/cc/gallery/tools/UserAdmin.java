package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class UserAdmin {
	public static void demo() throws Exception {
		DB db = new DB();
		Scanner in = new Scanner(System.in);
		int userInput;
		String user, password, name;
		db.menu();
		db.connect();
		userInput = in.nextInt();
		switch (userInput) {
			//list users
			case 1:
				db.listUsers();
				break;
			//add user
			case 2: 
				Scanner input = new Scanner(System.in);
				System.out.println("Enter username: ");
				user = input.nextLine();
				System.out.println("Enter password: ");
				password = input.nextLine();
				System.out.println("Enter full name: ");
				name = input.nextLine();
				db.addUser(user, password, name); 
				db.listUsers();
				break;
			//edit user
			case 3: 
				Scanner input2 = new Scanner(System.in);
                                System.out.println("Enter username to edit: ");
                                user = input2.nextLine();
                                System.out.println("New password: ");
       				password = input2.nextLine();
                                System.out.println("New full name: ");
                                name = input2.nextLine();
                                db.editUser(password, name, user);
				db.listUsers();
				break;
			//delete user
			case 4:
				Scanner input3 = new Scanner(System.in);
				System.out.println("Enter username to delete: ");
				user = input3.nextLine();
				System.out.println("Are you sure you want to delete " + user + "? (y/n)" );
				String result = input3.nextLine();
				if (result.charAt(0) == 'y' || result.charAt(0) == 'Y') {
					db.deleteUser(user);
					db.listUsers();
				} else
					System.out.println("Will not delete " + user + ".");
				break;
			//quit
			case 5:
				System.out.println("Quitting now.");
				break;
			//invalid inputs
			default:
				System.out.println("Invalid input.");
				break;

		}
		
		db.close();
	}
}
