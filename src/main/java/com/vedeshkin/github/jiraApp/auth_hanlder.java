package com.vedeshkin.github.jiraApp;

import com.atlassian.jira.rest.client.domain.BasicIssue;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vvedeshkin on 4/28/2017.
 */
public class auth_hanlder implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (request.queryParams("login").isEmpty()) return "Invalid user name";
        if (request.queryParams("password").isEmpty()) return "Invalid password";
        ArrayList<BasicIssue> issueList = App.
                getIssues(
                request.queryParams("login"),
                request.queryParams("password")
                );
        Map<String,Object> model = new HashMap<>();
        model.put("issueList",issueList);
        return new VelocityTemplateEngine().render(new ModelAndView(model,"result.vm"));
    }
}
