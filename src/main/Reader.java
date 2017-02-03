package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Author: Huijun Zhang
 * This WikiReader project is sending 100 GET requests to wikipedia main page with 100 threads and calculate response time 
 */
public class Reader {
	private final static int NUM_REQUESTS = 100;
	private final static String URL = "https://en.wikipedia.org/wiki/Main_Page";

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to WikiReader, this service will send 100 GET requests simultaneously to " + URL);
		
		List<Long> respTimeList = new ArrayList<>();
		WikiReadTask task = new WikiReadTask();
				
		for (int i = 0; i < NUM_REQUESTS; i++) {
			final int j = i;
			// create one thread for each request, so that threads won't wait for other threads
			Thread t = new Thread() {
				@Override
				public void run() {
					task.doGet(j);	
					long time;
					try {
						time = task.getTimeTaken();						
						respTimeList.add(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			};
			t.start();
			t.join();	// make sure all threads finish
		}

		// calculate results: mean and average
		long totalTime = 0l;
		for(long t : respTimeList) {
			totalTime += t;
		}
		Collections.sort(respTimeList);
		double mean = (respTimeList.get(NUM_REQUESTS / 2 - 1) + respTimeList.get(NUM_REQUESTS / 2)) / 2.0;			
		double avg = totalTime / NUM_REQUESTS;
		
		// standard deviation
		double stdSum = 0;
		for(int i = 0; i < respTimeList.size(); i++) {
			double cur = Math.pow(respTimeList.get(i) - avg, 2);
			stdSum += cur;
		}
		stdSum = Math.sqrt(stdSum);
		
		System.out.println("Start printing results in milliseconds:");
		System.out.println("10%: " + respTimeList.get(NUM_REQUESTS / 100 * 10 - 1));
		System.out.println("50%: " + respTimeList.get(NUM_REQUESTS / 100 * 50 - 1));
		System.out.println("90%: " + respTimeList.get(NUM_REQUESTS / 100 * 90 - 1));
		System.out.println("95%: " + respTimeList.get(NUM_REQUESTS / 100 * 95 - 1));
		System.out.println("99%: " + respTimeList.get(NUM_REQUESTS / 100 * 99 - 1));
		System.out.println("Average: " + avg);
		System.out.println("Mean: " + mean);
		System.out.println("Standard Deviation: " + stdSum);
	}
}
