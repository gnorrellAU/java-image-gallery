package edu.au.cc.gallery;
import edu.au.cc.gallery.User;
import edu.au.cc.gallery.UserDAO;
import edu.au.cc.gallery.Postgres;
import edu.au.cc.gallery.PostgresUserDAO;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Admin {
	DB db = new DB();

//	public String add(Request req, Response resp) {
//		return "The sum is " + (Integer.parseInt(req.queryParams("x")) + (Integer.parseInt(req.queryParams("y"))));
//	}

/*	public String calculator(Request req, Response resp) {
 		Map<String, Object> model = new HashMap<String, Object>();
                model.put("name", "Fred");
                return new HandlebarsTemplateEngine()
                        .render(new ModelAndView(model, "admin.hbs"));
	}*/
/*
	public String listUsers(Request req, Response res) throws SQLException {
		ArrayList<String> results = db.listUsers();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", results);
		return new HandlebarsTemplateEngine()
			.render(new ModelAndView(model, "admin.hbs"));	
	}

	public String updateUser(Request req, Response res) throws SQLException {
		String user = req.queryParams("user");
		String password = req.queryParams("password");
                String fullName = req.queryParams("full_name");
                db.editUser(password, fullName, user);
               res.redirect("/admin");
	       	return "User " + user + " was edited.";
	}

	public String editUser(Request req, Response res) throws SQLException {
		String user = req.queryParams("user");
		System.out.println(user);
		ArrayList<String> results = db.listUser(user);
	       System.out.println(results);	
		Map<String, Object> model = new HashMap<String, Object>();
                model.put("user", results);
                return new HandlebarsTemplateEngine()
                        .render(new ModelAndView(model, "editUser.hbs"));
//		String password = req.queryParams("password");
//		String fullName = req.queryParams("full_name");
//		db.editUser(user, password, fullName);
//		return "User " + user + " was edited.";
	}

	public String deleteUser(Request req, Response res) throws SQLException {
		String user = req.queryParams("user");
		db.deleteUser(user);
		 res.redirect("/admin");
		return "User was deleted.";
	}

	public String addUsers(Request req, Response res) throws SQLException {
		String username = req.queryParams("username");
		String password = req.queryParams("password");
		String fullName = req.queryParams("full_name");
		db.addUser(username, password, fullName);
		res.redirect("/admin");
		return "User was added."; 
	}
*/
private static UserDAO getUserDAO() {
        return Postgres.getUserDAO();
    }

    private static String listUsers() {
        try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("users", getUserDAO().getUsers());
                return new HandlebarsTemplateEngine()
                        .render(new ModelAndView(model, "users.hbs"));
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

	private String login(Request req, Response res) {
		Map<String, Object> model = new HashMap<String, Object>();
		return (new HandlebarsTemplateEngine()
				.render(new ModelAndView(model, "login.hbs")));
	}

	private String loginPost(Request req, Response res) {
		try {
			String username = req.queryParams("username");
			User u = getUserDAO().getUserByUsername(username);
			if (u == null || !u.getPassword().equals(req.queryParams("password"))) {
				res.redirect("/login");
				return "";
			}
			req.session().attribute("user", username);
			res.redirect("/debugSession");
		} catch (Exception ex) {
			return "Error: " + ex.getMessage();
		}
		return "";
	}
		
	private String debugSession(Request req, Response res) {
		StringBuffer sb = new StringBuffer();
		for (String key: req.session().attributes()) {
			sb.append(key+"->"+req.session().attribute(key)+"<br/>");
		}
		return sb.toString();
	}
	
	private boolean isAdmin(String username) {
		return username != null && username.equals("fred");
	}

	private String checkAdmin(Request req, Response resp) {
		if (!isAdmin(req.session().attribute("user")))
			resp.redirect("/login");
		return "";
	}



		public void addRoutes() throws SQLException {
	//	get("/admin/add", (req, res) -> add(req, res));
//		post("/admin/add", (req, res) -> add(req, res));
		db.connect();
        	get("/admin", (req, res) -> listUsers(req, res));
		get("/admin/add", (req, res) -> addUsers(req, res));
		get("/admin/edit", (req, res) -> editUser(req, res));
		get("/admin/edited", (req, res) -> updateUser(req, res));	
		get("/admin/delete", (req, res) -> deleteUser(req, res));	
		get("/login", (req, res) -> login(req, res));
		get("/debugSession", (req, res) -> debugSession(req, res));
		post("/login", (req, res) -> loginPost(req, res));
		before("/admin/*", (req, res) -> checkAdmin(req, res));
		  get("/admin/users/:username", (req, res) -> getUser(req.params(":username")));
        get("/admin/users", (req, res) -> listUsers());
        get("/admin/addUser/:username/:password/:fullName", (req, res) -> addUser(req.params(":username"), req.params(":password"), req.params(":fullName"), res));
	
		}
}

