package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
 * Author: Huijun Zhang
 * This WikiReader project is sending 100 GET requests to wikipedia main page with 100 threads and calculate response time 
 */
public class Reader {
	private final static int DEFAULT_NUM_REQUESTS = 100;
	private final static String URL = "https://en.wikipedia.org/wiki/Main_Page";

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to WikiReader, this service will send GET requests simultaneously to " + URL);		
		int num_requests = DEFAULT_NUM_REQUESTS;
		List<Long> respTimeList = new ArrayList<>();
		WikiReadTask task = new WikiReadTask();
		boolean printLog = false;
		
		// get number of requests from user input
		num_requests = getUserInputNumRequests();
		printLog = getUserInputPrintLog();
		
		// start sending requests
		for (int i = 0; i < num_requests; i++) {
			final int j = i;
			final boolean print = printLog;
			// create one thread for each request, so that threads won't wait for other threads
			Thread t = new Thread() {
				@Override
				public void run() {
					task.doGet(j, print);	
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

		// calculate and print results
		calculatePrintResult(respTimeList, num_requests);
	}
	
	private static int getUserInputNumRequests() {
		int num_requests = DEFAULT_NUM_REQUESTS;		
		try {
			Scanner scanner = new Scanner(System.in);
			int input_number;
			do {
			    System.out.println("How many requests do you want to send? Please enter a number from 1 - 100");
			    while (!scanner.hasNextInt()) {
			        System.out.println("That's not valid number!");
			        scanner.next();
			    }
			    input_number = scanner.nextInt();
			} while (input_number < 1 || input_number > 100);
			System.out.println("Thank you! Got number " + input_number);
			num_requests = input_number;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		return num_requests;
	}
	
	private static boolean getUserInputPrintLog() {
		boolean print = false;		
		try {
			Scanner scanner = new Scanner(System.in);
			String input;
			do {
			    System.out.println("Do you want to print response page content? Please enter Y/N");
			    input = scanner.nextLine();
			    input = input.trim().toUpperCase();
			} while (!input.equals("Y") && !input.equals("N"));
			System.out.println("Thank you! Got input " + input);
			if(input.equals("Y")) {
				print = true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		return print;
	}
	
	private static void calculatePrintResult(List<Long> respTimeList, int num_requests) {
		if(respTimeList == null || respTimeList.size() == 0 || num_requests == 0)	return;
		
		// sort result collection
		Collections.sort(respTimeList);
		
		// calculate results: mean and average
		long totalTime = 0l;
		for(long t : respTimeList) {
			totalTime += t;
		}
				
		double mean = 0;
		if(num_requests % 2 == 0) {
			mean = (respTimeList.get(num_requests / 2 - 1) + respTimeList.get(num_requests / 2)) / 2.0;		
		} else {
			mean = respTimeList.get(num_requests / 2);
		}
		double avg = totalTime / num_requests;
		
		// standard deviation
		double stdSum = 0;
		for(int i = 0; i < respTimeList.size(); i++) {
			double cur = Math.pow(respTimeList.get(i) - avg, 2);
			stdSum += cur;
		}
		stdSum = Math.sqrt(stdSum);
				
		// print results
		System.out.println("Start printing results in milliseconds:");
		System.out.println("10%: " + respTimeList.get((int)(num_requests / 100.0 * 10)));
		System.out.println("50%: " + respTimeList.get((int)(num_requests / 100.0 * 50)));
		System.out.println("90%: " + respTimeList.get((int)(num_requests / 100.0 * 90)));
		System.out.println("95%: " + respTimeList.get((int)(num_requests / 100.0 * 95)));
		System.out.println("99%: " + respTimeList.get((int)(num_requests / 100.0 * 99)));
		System.out.println("Average: " + avg);
		System.out.println("Mean: " + mean);
		System.out.println("Standard Deviation: " + stdSum);
		System.out.println("Thank you for using WikiReader, have a nice day!");		
	}
}
