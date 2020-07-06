package edu.au.cc.gallery;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import edu.au.cc.gallery.Postgres;
import edu.au.cc.gallery.UserDAO;
import edu.au.cc.gallery.User;
import edu.au.cc.gallery.Photo;
import edu.au.cc.gallery.PhotoDAO;
import edu.au.cc.gallery.PostgresPhotoDAO;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Admin {
	
	private static UserDAO getUserDAO() throws Exception {
		return Postgres.getUserDAO();
	}

	private static PhotoDAO getPhotosDAO() throws Exception {
		return Postgres.getPhotosDAO();
	}

	private static String listUsers() {
		try {
		//	UserDAO dao = getUserDAO();
			List<User> results = getUserDAO().getUsers();
			Map<String, Object> model = new HashMap<String, Object>();
			List<Map<String, Object>> userList = new ArrayList<>();
			for (User usr: results) {
				Map<String, Object> users = new HashMap<String, Object>();
				users.put("username", usr.getUsername());
				users.put("password", usr.getPassword());
				users.put("fullName", usr.getFullName());
				System.out.println(usr.getUsername() + " "+ usr.getPassword() + " "+usr.getFullName());
				userList.add(users);
			}
		//	model.put("name", getUserDAO().getUsers());
			model.put("users", userList);
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
				System.out.println("redirecting");
                                res.redirect("/login");
                                return "";
                        }
                        req.session().attribute("user", username);
                        //administrative user redirect
			if (isAdmin(username)) {
				res.redirect("/admin/users");
			} else {
				res.redirect("/photos");
                	}
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

	private void checkAdmin(Request req, Response res) {
		String s = req.session().attribute("user");
		System.out.println(s);	
		if (!isAdmin(req.session().attribute("user"))) {
			//res.redirect("/photos/:{s}");
			halt();
		}
	}
	private String deleteUser(Request req, Response res) {
		Map<String, Object> model = new HashMap<>();
		model.put("title", "Delete User");
		model.put("message", "Are you sure that you want to delete this user?");
		model.put("onYes", "/admin/deleteUserExec/"+req.params(":username"));
		model.put("onNo", "/admin/users");
		return new HandlebarsTemplateEngine()
			.render(new ModelAndView(model, "confirm.hbs"));
	}

	private String deleteUserExec(Request req, Response res) {
		try {
			getUserDAO().deleteUser(req.params(":username"));
			res.redirect("/admin/users");
			return null;
		} catch (Exception e) {
			return "Error: "+e.getMessage();
		}
	}

	public String editUser(Request req, Response res) {
		try {
		Map<String, Object> model = new HashMap<String, Object>();
		User u = getUserDAO().getUserByUsername(req.params(":username"));
		System.out.println(u);
		model.put("user", u);
                return (new HandlebarsTemplateEngine()
                                .render(new ModelAndView(model, "edit.hbs")));
		} catch (Exception e) {
			return "Error: "+ e.getMessage();
		}
	}

	 public String editUserPost(Request req, Response res) {
                try {
		 	getUserDAO().editUser(req.params(":username"), req.queryParams("password"), req.queryParams("full_name"));
             		res.redirect("/admin/users");
			return null;
	       	} catch (Exception e) {
              		return "Error: "+e.getMessage();
              	}
        }

	public String userPhotos(Request req, Response res) {
		try {
			String s = req.session().attribute("user");
			List<Photo> p = getPhotosDAO().getPhotos(s);
			System.out.println(p);
			Map<String, Object> model = new HashMap<String, Object>();
			  List<Map<String, Object>> photoList = new ArrayList<>();
			for (Photo pic : p) {
				Map<String, Object> pics = new HashMap<String, Object>();
			//	pics.put("username", pic.getUsername());
				pics.put("photoName", pic.getImageName());
				System.out.println(pic.getUsername() + pic.getImageName());
				photoList.add(pics);
			}	
			model.put("photos", photoList);
			return (new HandlebarsTemplateEngine()
               	                 .render(new ModelAndView(model, "photos.hbs")));
		} catch (Exception e) {
			return "Error: "+e.getMessage();
		}
	
	}

	public String userPhotosPost(Request req, Response res) {
		try {
			req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
   			try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
       			// Use the input stream to create a file
			System.out.println(is);
			PhotoDAO dao = getPhotosDAO();
                        dao.addPhoto(new User(username, is));
    		}			
			res.redirect("/photos");
   			 return "File uploaded";
		//	System.out.println(photo);			
			//return "";
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

	public void addRoutes() {
		get("/login", (req, res) -> login(req, res));
                post("/login", (req, res) -> loginPost(req, res));
		get("/debugSession", (req, res) -> debugSession(req, res));
		before("/admin/*", (req, res) -> checkAdmin(req, res));	
		get("/photos", (req, res) -> userPhotos(req, res));
		post("/photos", (req, res) -> userPhotosPost(req, res));
		get("/admin/users", (req, res) -> listUsers());
		get("/admin/deleteUser/:username", (req, res) -> deleteUser(req, res));
		get("/admin/deleteUserExec/:username", (req, res) -> deleteUserExec(req, res));
		get("/admin/users/:username", (req, res) -> getUser(req.params(":username")));
		get("/admin/addUser/:username/:password/:fullName", (req, res) -> addUser(req.params(":username"), req.params(":password"), req.params(":fullName"), res));
		get("/admin/users/:username", (req, res) -> getUser(req.params(":username")));
		get("/admin/edit/:username", (req, res) -> editUser(req, res));
		post("/admin/edit/:username", (req, res) -> editUserPost(req, res));
	}
}
