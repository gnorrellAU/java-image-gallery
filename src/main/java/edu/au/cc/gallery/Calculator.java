package edu.au.cc.gallery;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Calculator {

	public String add(Request req, Response resp) {
		return "The sum is " + (Integer.parseInt(req.queryParams("x")) + (Integer.parseInt(req.queryParams("y"))));
	}

	public String calculator(Request req, Response resp) {
 		Map<String, Object> model = new HashMap<String, Object>();
                model.put("name", "Fred");
                return new HandlebarsTemplateEngine()
                        .render(new ModelAndView(model, "calculator.hbs"));
	}

	public void addRoutes() {
		post("/add", (req, res) -> add(req, res));

        	get("/calculator", (req, res) -> calculator(req, res));
	}
}
