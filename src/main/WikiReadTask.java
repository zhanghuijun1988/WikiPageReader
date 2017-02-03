package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiReadTask {
	private final static String URL = "https://en.wikipedia.org/wiki/Main_Page";
	private final static String METHOD = "GET";
	private final static int HTTP_OK = 200;

	private Long responseTime = 0l;
	private Long startTime = 0l;
	private Long endTime = 0l;

	public synchronized void doGet(int index) {
		startTime = System.currentTimeMillis();
		try {
			// create connection
			URL obj = new URL(URL);			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(METHOD);
			
			// process response
			int responseCode = con.getResponseCode();			
			if(responseCode != HTTP_OK) {
				throw new Exception("WikiReadTask http response exception");
			}

			// print response from input stream
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String inputLine;
			
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);	//sb has the response content, which can be used for debugging/logging purpose
			}			
			br.close();

		} catch (Exception e) {
			// Exception case, do proper logging
			System.out.println(e.getMessage());
		} finally {
			// calculate response time for all cases
			endTime = System.currentTimeMillis();
			calculateTime(index);
		}
	}

	public synchronized void calculateTime(int index) {
		responseTime = endTime - startTime;
		System.out.println("Thread " + index + " - Response Time:  " + responseTime);
	}

	public synchronized long getTimeTaken() throws InterruptedException {
		return responseTime;
	}

}
