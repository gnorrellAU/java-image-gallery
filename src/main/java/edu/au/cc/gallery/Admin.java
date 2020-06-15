package edu.au.cc.gallery;

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

	public String listUsers(Request req, Response res) throws SQLException {
		ArrayList<String> results = db.listUsers();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", results);
		return new HandlebarsTemplateEngine()
			.render(new ModelAndView(model, "admin.hbs"));	
	}

	public String editUser(Request req, Response res) throws SQLException {
		String user = req.queryParams("user");
	//	Map<String, Object> model = new HashMap<String, Object>();
          //      model.put("user", user);
            //    return new HandlebarsTemplateEngine()
              //          .render(new ModelAndView(model, "getUser.hbs"));
		String password = req.queryParams("password");
		String fullName = req.queryParams("full_name");
		db.editUser(user, password, fullName);
		return "User " + user + " was edited.";
	}

	public String deleteUser(Request req, Response res) throws SQLException {
		String user = req.queryParams("user");
		db.deleteUser(user);
		return "User was deleted.";
	}

	public String addUsers(Request req, Response res) throws SQLException {
		String username = req.queryParams("username");
		String password = req.queryParams("password");
		String fullName = req.queryParams("full_name");
		db.addUser(username, password, fullName);
		return "User was added."; 
	}
		
		public void addRoutes() throws SQLException {
	//	get("/admin/add", (req, res) -> add(req, res));
//		post("/admin/add", (req, res) -> add(req, res));
		db.connect();
        	get("/admin", (req, res) -> listUsers(req, res));
		get("/admin/add", (req, res) -> addUsers(req, res));
		get("/admin/edit", (req, res) -> editUser(req, res));
	
		get("/admin/delete", (req, res) -> deleteUser(req, res));	
	}
}
