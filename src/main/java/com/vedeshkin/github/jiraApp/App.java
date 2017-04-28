package com.vedeshkin.github.jiraApp;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import spark.Spark;
import spark.utils.IOUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static spark.Spark.port;

/**
 * Created by vvedeshkin on 4/27/2017.
 */

public class App {
    public static void main(String[] args) {
        port(8888);
        Spark.get("/",(req , res)-> IOUtils.toString(App.class.getResourceAsStream("/resources/login_form.html")));
        Spark.post("/get_data", new auth_hanlder());
    }

    public static ArrayList<BasicIssue> getIssues(String user, String password){
        ArrayList<BasicIssue> issues = new ArrayList<>();
        try {
            JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
            URI jiraUrl = new URI("https://jira.axiomsl.us/");
            JiraRestClient client = factory.createWithBasicHttpAuthentication(jiraUrl, user, password);

            Promise<SearchResult> searchResultPromise = client
                    .getSearchClient()
                    .searchJql("assignee=currentuser() AND updated >=startOfDay(-0d) ");
            for (BasicIssue issue : searchResultPromise.claim().getIssues())
            {
                issues.add(issue);
            }
            return issues;
        }catch (URISyntaxException ex)
        {
            ex.printStackTrace();
            return  null;
        }
    }
}
