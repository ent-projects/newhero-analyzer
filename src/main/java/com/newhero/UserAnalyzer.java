/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.newhero.utils.ClientUtils;
import com.newhero.utils.ConvertorUtils;


/**
 * @author wuheng09@gmail.com
 *
 */
public class UserAnalyzer extends AbstractAnalyzer {

	public final static String SQL = "INSERT INTO users(name, nickname, email, created) values(?,?,?,?)";
	
	public static void main(String[] args) throws Exception {
		
		PreparedStatement ps = ClientUtils.createStatement("gitlab", SQL);
		
		ArrayNode usersNoe = new ObjectMapper().createArrayNode();
		
		JsonNode users = gitClient.getResource(createRequest("User", null));
		
		for (int i = 0; i< users.size(); i++) {
			ObjectNode userNode = new ObjectMapper().createObjectNode();
			userNode.put("name", users.get(i).get("name").asText());
			userNode.put("nickname", users.get(i).get("username").asText());
			userNode.put("email", users.get(i).get("email").asText());
			userNode.put("join", users.get(i).get("created_at").asText());
			usersNoe.add(userNode);
			
			ps.setString(1, users.get(i).get("name").asText());
			ps.setString(2, users.get(i).get("username").asText());
			ps.setString(3, users.get(i).get("email").asText());
			Timestamp timestamp = ConvertorUtils.toTimestamp(users.get(i).get("created_at").asText());
			ps.setTimestamp(4, timestamp);
			ps.executeUpdate();
			
		}
		ps.close();
		
		System.out.println(usersNoe.toPrettyString());
	}

}
