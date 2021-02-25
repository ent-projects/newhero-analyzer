/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.devops.gitlab.GitlabClient;


/**
 * @author wuheng09@gmail.com
 *
 */
public class AbstractAnalyzer {

	static String GITLAB_URL = "http://192.168.6.221/";
	
	static String GITLAB_TOKEN = "gkb_JWa4AidtsLBLVgNr";

	public static GitlabClient createClient() {
		return new GitlabClient(GITLAB_URL, GITLAB_TOKEN);
	}
	
	protected static ObjectNode createRequest(String kind, String[] params) {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("kind", kind);
		if (params != null) {
			node.set("params", createParams(params));
		}
		return node;
	}
	
	
	protected static ArrayNode createParams(String[] params) {
		ArrayNode node = new ObjectMapper().createArrayNode();
		for (int i = 0; i < params.length ; i = i+2) {
			ObjectNode nn = new ObjectMapper().createObjectNode();
			nn.put("name", params[i]);
			nn.put("value", params[i+1]);
			node.add(nn);
		}
		return node;
	}
	
	public static void print(String title, JsonNode json) {
		System.out.println(title + "----------------------------");
		System.out.println(json.toPrettyString());
	}
	
	public static int getDayDiffer(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
        long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
        return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }
}
