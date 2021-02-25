/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.devops.gitlab.GitlabClient;


/**
 * @author wuheng09@gmail.com
 *
 */
public class UserAnalyzer extends AbstractAnalyzer {


	static GitlabClient client = createClient();
	
	public static void main(String[] args) throws Exception {
		
		ArrayNode usersNoe = new ObjectMapper().createArrayNode();
		
		JsonNode users = client.getResource(createRequest("User", null));
		
		for (int i = 0; i< users.size(); i++) {
			ObjectNode userNode = new ObjectMapper().createObjectNode();
			userNode.put("name", users.get(i).get("name").asText());
			userNode.put("username", users.get(i).get("username").asText());
			userNode.put("email", users.get(i).get("email").asText());
			usersNoe.add(userNode);
		}
		
		System.out.println(usersNoe.toPrettyString());
	}

}
