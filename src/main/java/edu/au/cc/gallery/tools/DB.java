package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class DB {

	private static final String dbUrl = "jdbc:postgresql://image-gallery.ceza2uidpxb7.us-east-1.rds.amazonaws.com/image_gallery";
        private Connection connection;

  //	Secrets Manager
	private JSONObject getSecret() {
		String s = Secrets.getSecretImageGallery();
		return new JSONObject(s);
	}
	
	private String getPassword(JSONObject secret) {
		return secret.getString("password");
	}

        public void connect() throws SQLException {
                try {
                        Class.forName("org.postgresql.Driver");
			JSONObject secret = getSecret();
                        connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword(secret));
                } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        System.exit(1);
                }

        } 
/*

        private String getPassword() {
                try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))) {
                        String result = br.readLine();
                        return result;
                } catch (IOException ex) {
                        System.err.println("Error opening password file. Make sure .sql-passwd exists and contains your sql password.");
                        System.exit(1);
                }
                return null;
        }

        public void connect() throws SQLException {
                try {
			System.out.println("connecting");
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword());
			System.out.println("connected");
                } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        System.exit(1);
                }

        }
*/
	public ArrayList<String> listUser(String user) throws SQLException {
                PreparedStatement stmt = connection.prepareStatement("select username, password, full_name from users where username=?");
		stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();
                ArrayList<String> result = new ArrayList<String>();
                while(rs.next()) {
                        //System.out.println("added row");
                      /*        result = result.concat("username: " + rs.getString(1) + "\n"
                                          + "password: " + rs.getString(2) + "\n"
                                          + "full name: " + rs.getString(3)
                                          + "\n----------------------------------------\n");
               */
                        result.add(rs.getString(1));
			result.add(rs.getString(2));
			result.add(rs.getString(3));
                }
                rs.close();
                return result;
}

public ArrayList<String> listUsers() throws SQLException {
                PreparedStatement stmt = connection.prepareStatement("select username, password, full_name from users");
                ResultSet rs = stmt.executeQuery();
		ArrayList<String> result = new ArrayList<String>();
		while(rs.next()) {
                     	//System.out.println("added row");
		      /*	result = result.concat("username: " + rs.getString(1) + "\n"
                                          + "password: " + rs.getString(2) + "\n"
                                          + "full name: " + rs.getString(3)
                                          + "\n----------------------------------------\n");
               */
			result.add(rs.getString(1));       
		}
                rs.close();
		return result;       
}

        public boolean doesExist(String username) throws SQLException {
                PreparedStatement stmt = connection.prepareStatement("SELECT * from users where username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
        //      System.out.println(exists.getString(1));
                boolean user = false;
                if (rs.next()) {
                        user = true;
                }
                rs.close();
                return user;

        }

	public void addUser(String user, String pass, String fullName) throws SQLException {
                boolean exists = doesExist(user);
                System.out.println(user);
                      if (!exists) {
                               PreparedStatement stmt = connection.prepareStatement("insert into users (username, password, full_name) values (?, ?, ?)");
                        stmt.setString(1, user);
                        stmt.setString(2, pass);
                        stmt.setString(3, fullName);
                        stmt.execute();
                        System.out.println("User was added.");
                      } else
                             System.out.println("User with the username, " + user + ", already exists");
        }

        public void editUser(String pass, String fullName, String user) throws SQLException {
               boolean exists = doesExist(user);
	       if (exists) {
			if (pass == null || pass.length() == 0) {
				PreparedStatement ps = connection.prepareStatement("select password from users where username = ?");
				ps.setString(1, user);
                		ResultSet rs = ps.executeQuery();
        			if (rs.next()) {
					pass = rs.getString(1);
					System.out.println(pass);
				}
			}
			if (fullName == null || fullName.length() == 0)	{			
				PreparedStatement pstmt  = connection.prepareStatement("select full_name from users where username = ?");
                                pstmt.setString(1, user);
                                ResultSet rs = pstmt.executeQuery();
                                if (rs.next()) {
					fullName = rs.getString(1);
					System.out.println(fullName);
                		}        
			} 

	               	PreparedStatement stmt = connection.prepareStatement("UPDATE users "
                                               + "SET password = ?, full_name = ? "
                                               + "WHERE username = ?");
           		stmt.setString(1, pass);
                	stmt.setString(2, fullName);
               		stmt.setString(3, user);
                	stmt.execute();
			
	       } else 
		       System.out.println("User with the username, " + user + " does not exist.");
        }

        public void deleteUser(String user) throws SQLException {
             	boolean exists = doesExist(user);
              	if (exists) {
                	PreparedStatement stmt = connection.prepareStatement("delete from users where username = ?");
                	stmt.setString(1, user);
                	stmt.execute();
		} else
			 System.out.println("User with the username, " + user + " does not exist.");
        }

        public void close() throws SQLException {
                connection.close();
        }

	    public void menu() {
                System.out.println("What would you like to do?"
                                + "\n1) List Users"
                                + "\n2) Add User"
                                + "\n3) Edit User"
                                + "\n4) Delete User"
                                + "\n5) Quit"
                                + "\nEnter Command: ");
        }


}
