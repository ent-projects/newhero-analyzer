/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero;



import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.devops.gitlab.GitlabClient;


/**
 * @author wuheng09@gmail.com
 *
 */
public class ProjectAnalyzer extends AbstractAnalyzer {

	static Map<String, Integer> weekMapper = new LinkedHashMap<>();
	
	static Map<String, Integer> monthMapper = new LinkedHashMap<>();
	
	static Map<String, Integer> longMapper = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {
		JsonNode projNodes = projSummary(gitClient);
		projDetail(gitClient, projNodes);
		ArrayNode json = new ObjectMapper().createArrayNode();
		for (String key: longMapper.keySet()) {
			ObjectNode node = new ObjectMapper().createObjectNode();
			node.put("name", key);
			node.put("latest_7_days", weekMapper.get(key));
			node.put("latest_30_days", monthMapper.get(key));
			node.put("from_start", longMapper.get(key));
			json.add(node);
		}
		
		print("������Ա", json);
	}

	public static void projDetail(GitlabClient client, JsonNode projNodes) throws Exception, ParseException {
		for (int i = 0; i < projNodes.get("summary").size(); i++) {
			ObjectNode projNode = new ObjectMapper().createObjectNode();
			JsonNode proj = projNodes.get("summary").get(i);
			{
				JsonNode members = client.getResource(createRequest("ProjectMember", 
									new String[] {"id", proj.get("id").asText()}));
				
				ArrayNode membersNode = new ObjectMapper().createArrayNode();
				for (int j = 0; j < members.size(); j++) {
					membersNode.add(members.get(j).get("name").asText());
				}
				projNode.set("developer", membersNode);
			}
			
			{
				int commit = getCommitsWithDays(client, proj.get("id").asText(), 6000, longMapper);
				projNode.put("start", proj.get("start").asText());
				int dayDiffer = getDayDiffer(
						new SimpleDateFormat("yyyy-MM-dd")
						.parse(proj.get("start").asText()), new Date());
				projNode.put("duration", dayDiffer + "��");
				projNode.put("commits_latest_7_days", getCommitsWithDays(client, proj.get("id").asText(), 7, weekMapper) + "��");
				projNode.put("commits_latest_30_days", getCommitsWithDays(client, proj.get("id").asText(), 30, monthMapper) + "��");
				projNode.put("commits_from_start", commit + "��");
				projNode.put("average_from_start", new DecimalFormat("0.00").format(
									((float) commit)/dayDiffer) + "��/��");
			}
			
			print(proj.get("name").asText(), projNode);
		}
	}
	
	/********************************************************
	 * 
	 *            Core
	 * 
	 *********************************************************/

	public static JsonNode projSummary(GitlabClient client) throws Exception {
		
		JsonNode projects = client.getResource(createRequest("Project", null));
		ObjectNode projNodes = new ObjectMapper().createObjectNode();
		projNodes.put("total", projects.size() + "����Ŀ");
		ArrayNode projList = new ObjectMapper().createArrayNode();
		for (int i = 0; i < projects.size(); i++) {
			JsonNode proj = projects.get(i);
			ObjectNode projNode = new ObjectMapper().createObjectNode();
			projNode.put("id", proj.get("id").asText());
			projNode.put("name", proj.get("name").asText());
			projNode.put("owner", proj.get("namespace").get("name").asText());
			projNode.put("start", proj.get("created_at").asText().substring(0, 10));
			projList.add(projNode);
		}
		projNodes.set("summary", projList);
		print("��Ŀ���", projNodes);
		return projNodes;
	}

	/********************************************************
	 * 
	 *            Utils
	 * 
	 *********************************************************/
	
	
	public static int getCommitsWithDays(GitlabClient client, String projId, int day, Map<String, Integer> mapper) throws Exception {
		int num = 0;
		for (int i = 1; i <= 500; i++) {
			
			JsonNode commits = client.getResource(createRequest("ProjectCommit", 
				new String[] {"id", projId, "page", String.valueOf(i)}));
			
			if (commits.size() == 0) {
				break;
			}
			
			for (int j = 0; j < commits.size(); j++) {
				String submitted = commits.get(j).get("committed_date")
										.asText().substring(0, 10);
				int dayDiffer = getDayDiffer(
						new SimpleDateFormat("yyyy-MM-dd")
						.parse(submitted), new Date());
				if (dayDiffer > day) {
					break;
				}
				num++;
				stats(commits.get(j), mapper);
			}
		}
		return num;
	}
	
	public static void stats(JsonNode commit, Map<String, Integer> mapper) {
		try {
		String developer = commit.get("committer_name").asText();
		Integer cnt = mapper.get(developer) == null ? 0 : mapper.get(developer);
		cnt++;
		mapper.put(developer, cnt);
		} catch (Exception ex) {
			System.err.println(commit.toPrettyString());
		}
	}
}
