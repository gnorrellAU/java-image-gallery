/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.au.cc.gallery;

import java.util.Map;
import java.util.HashMap;

import edu.au.cc.gallery.Postgres;
import edu.au.cc.gallery.UserDAO;
import edu.au.cc.gallery.User;

import spark.Response;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class App {

    public String getGreeting() {
        return "Hello Ginger.";
    }
/*	
    private static UserDAO getUserDAO() {
	return Postgres.getUserDAO();
    }

    private static String listUsers() {
	try {
	    	StringBuffer sb = new StringBuffer();
		UserDAO dao = getUserDAO();
		for(User u: dao.getUsers())
			sb.append(u.toString());
		return sb.toString();
	} catch (Exception e) {
		return "Error: " + e.getMessage();
	}
    }

    private static String getUser(String username) {
	    try {
		    UserDAO dao = getUserDAO();
		    return dao.getUserByUsername(username).toString();
	    } catch (Exception ex) {
		    return "Error: "+ ex.getMessage();
	    }
    }

    private static String addUser(String username, String password, String fullName, Response r) {
	try {
                    UserDAO dao = getUserDAO();
                    dao.addUser(new User(username, password, fullName));
		    r.redirect("/users");
		    return "";
            } catch (Exception ex) {
                    return "Error: "+ ex.getMessage();
            }
    }
*/
    public static void main(String[] args) throws Exception {
      DB db = new DB();
      db.connect();
	    // System.out.println(new App().getGreeting());
       //UserAdmin.demo();
	String portString = System.getenv("JETTY_PORT");
	if (portString == null || portString.equals(""))
		port (5000);	
    	else
		port(Integer.parseInt(portString));
	//  port(5000);
    	    get("/", (req, res) -> "Hello World");

//	get("/goodbye", (req, rest) -> "Goodbye");

//	get("/greet/:name", (req, res) -> {
//		return "Hello " + req.params(":name");
//	});
	//String result = db.listUsers();
//	System.out.println(result);	
//	get("/users/:username", (req, res) -> getUser(req.params(":username")));
//	get("/users", (req, res) -> listUsers());
//	get("/addUser/:username/:password/:fullName", (req, res) -> addUser(req.params(":username"), req.params(":password"), req.params(":fullName"), res));
	new Admin().addRoutes();
    }
}
