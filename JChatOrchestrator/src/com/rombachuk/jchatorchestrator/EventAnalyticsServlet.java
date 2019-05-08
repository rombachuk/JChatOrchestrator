package com.rombachuk.jchatorchestrator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.cloud.sdk.core.service.exception.UnauthorizedException;
import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;




/**
 * Servlet implementation class chatServlet
 */

public class EventAnalyticsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventAnalyticsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
  
 	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("relatedevents.jsp");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JsonObject botException = new JsonObject();
		 try {
			    // ChatFilter provides validated set of attributes for use by chat servlet
			    // get request variables - specific to this request
			    String chatname = (String) request.getAttribute("chatname"); //added by filter
			    String dialogueassistantid = (String) request.getAttribute("dialogueassistantid"); //added by filter
			    String dialoguesessionid = (String) request.getAttribute("dialoguesessionid"); //added by filter
			 	JsonObject chatclientAssistantInput = (JsonObject) request.getAttribute("chatclientassistantinput"); //added by filter
			 	JsonObject chatclientAppInput = (JsonObject) request.getAttribute("chatclientappinput"); //added by filter
			    MessageContext latestContext = (MessageContext) request.getAttribute("latestcontext");	
			    

			 	
			 	//get session variables - maintained over many requests in this session
			 	WatsonConnection watsonconnection = (WatsonConnection) request.getSession().getAttribute("watsonconnection");
			    String chatuuid_lastreply = request.getParameter("chatid")+"lastreply";
			    MessageResponse lastReply = (MessageResponse) request.getSession().getAttribute(chatuuid_lastreply);


			    
			    //send message to watson assistant
			    MessageOptions options = new MessageOptions.Builder(dialogueassistantid,dialoguesessionid).build();
			    MessageInput input = new MessageInput.Builder()
			    		  .messageType("text")
			    		  .text(chatclientAssistantInput.get("input").getAsString())
			    		  .build();
			    
	
			    
				if ((lastReply == null) ) {
						   options= new MessageOptions.Builder(dialogueassistantid,dialoguesessionid)
								    .input(input)
								    .build();
				} else {
				  	    options = new MessageOptions.Builder(dialogueassistantid,dialoguesessionid)
					    .input(input)
					    //.intents(lastReply.getIntents())
					    //.entities(lastReply.getEntities())
					    .context(latestContext)
					    //.output(lastReply.getOutput())
					    .build();
			    }
				MessageResponse botReply = watsonconnection.synchronousRequest(options);

				// process reply from watson assistant - 
				
				// appData is private to the app and the client, assistant does not see it.
				
				JsonObject appData = new JsonObject();
			    if (chatclientAppInput.has("parameters")) {
			    	appData.add("parameters", chatclientAppInput.getAsJsonArray("parameters"));
			    }

			    // context is shared with assistant. 
			    // context holds activity,operation,operationstatus which are set by client+bot
			    // app responds to the operation+status and uses its private appdata 

			    MessageContext context = botReply.getContext();
				if ((context.getSkills().containsKey("activity") == true ) && (context.getSkills().containsKey("operation") == true )){
					
					// collectparameter operations 
					if (context.getSkills().get("operation").toString().equals("collectparameters") && 
							!context.getSkills().get("operationstatus").toString().equals("complete")) {
						
						if (context.getSkills().get("activity").toString().equals("searchseasonalevents") ||
						 context.getSkills().get("activity").toString().equals("searchrelatedevents")) {
					     // appdata activity
					    }
						
						if (context.getSkills().get("activity").toString().equals("searchhistoricevents")) {
					     // appdata activity
						 HistoricEventsConnection historyconn = (HistoricEventsConnection) request.getSession().getServletContext().getAttribute("eventbothistoryconnection");
						 appData.add("filter_fields",historyconn.fields);
					    }

					    if (context.getSkills().get("activity").toString().equals("searchcurrentevents")) {
					     // appdata activity
						 JsonArray currentfields = (JsonArray) request.getSession().getServletContext().getAttribute("eventbotobjectserverfields");
						 appData.add("filter_fields",currentfields);
					    }
					}
					
				}
				
				// send app specific response to chatclient via ChatFilter
				request.setAttribute("appdata", appData);
				// send bot response to chatclient via ChatFilter
				request.setAttribute("botreply", botReply);
			 }
		 
		     catch( UnauthorizedException e) {
		    	 System.out.println(e.toString());
		    	 botException.addProperty("error","Assistant Access Authorisation problem"); 
		    	 request.setAttribute("botexception",botException);
		     }
	         catch (Exception e) {
	        	 System.out.println(e.toString());
		    	 botException.addProperty("error","Assistant Request error"); 
		    	 
		    	 request.setAttribute("botexception",botException);
		     }

	}

}
