/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.github.kubesys.devops.gitlab.GitlabClient;


/**
 * @author wuheng09@gmail.com
 *
 */
public class ClientUtils {

	static String GITLAB_URL = "http://192.168.6.221/";
	
	static String GITLAB_TOKEN = "9jaQtmPrxvFjR6hzECoz";

	public static GitlabClient createClient() {
		return new GitlabClient(GITLAB_URL, GITLAB_TOKEN);
	} 
	
	static String POSTGRESQL_URL  = "jdbc:postgresql://192.168.6.89:30306/";
	
	static String POSTGRESQL_USER = "postgres";
	
	static String POSTGRESQL_PWD  = "onceas";
	
	public static Connection createConn(String db) throws Exception {
		Class.forName("org.postgresql.Driver").newInstance();
		String url = POSTGRESQL_URL + db;
		return DriverManager.getConnection(url, 
				POSTGRESQL_USER, POSTGRESQL_PWD);
	}
	
	public static PreparedStatement createStatement(String db, String sql) throws Exception {
		return createConn(db).prepareStatement(sql);
	}
}
