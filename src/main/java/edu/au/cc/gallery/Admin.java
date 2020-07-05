package edu.au.cc.gallery;

import java.util.Map;
import java.util.HashMap;

import edu.au.cc.gallery.Postgres;
import edu.au.cc.gallery.UserDAO;
import edu.au.cc.gallery.User;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.handlebars.HandlebarsTemplateEngine;



public class Admin {
	
	private static UserDAO getUserDAO() throws Exception {
		return Postgres.getUserDAO();
	}

	private static String listUsers() {
		try {
		//	UserDAO dao = getUserDAO();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("name", getUserDAO().getUsers());
			return new HandlebarsTemplateEngine()
				.render(new ModelAndView(model, "admin.hbs"));
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

	private static String getUser(String username) {
		try {
			UserDAO dao = getUserDAO();
			return dao.getUserByUsername(username).toString();
		} catch (Exception e) {
			return "Error " + e.getMessage();
		}
	}

	private static String addUser(String username, String password, String fullName, Response r) {
		try {
			UserDAO dao = getUserDAO();
			dao.addUser(new User(username, password, fullName));
			r.redirect("/users");
			return "";
		} catch (Exception e) {
			return "Error" + e.getMessage();
		}
	}

	private String login(Request req, Response res) {
                Map<String, Object> model = new HashMap<String, Object>();
                return (new HandlebarsTemplateEngine()
                                .render(new ModelAndView(model, "login.hbs")));
        }

       private String loginPost(Request req, Response res) {
                try {
                        UserDAO dao = getUserDAO();
                        String username = req.queryParams("username");
                        User u = dao.getUserByUsername(username);
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

	public void addRoutes() {
		get("/login", (req, res) -> login(req, res));
                get("/debugSession", (req, res) -> debugSession(req, res));
	//	before("/admin/*", (req, res) -> checkAdmin(req, res));	
		get("/admin/users", (req, res) -> listUsers());
		get("/admin/users/:username", (req, res) -> getUser(req.params(":username")));
		get("/admin/addUser/:username/:password/:fullName", (req, res) -> addUser(req.params(":username"), req.params(":password"), req.params(":fullName"), res));
	}
}
