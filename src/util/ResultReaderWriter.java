package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//@author Karol Cichosz
public class ResultReaderWriter {

	public static void writeResultsToFile(List<Set<Integer>> results, String filePathToWrite) {
		System.out.println("Printing results to: " + filePathToWrite + " file");

		try {
			PrintWriter writer = new PrintWriter(filePathToWrite);

			long startTime;
			for (int i = 0; i < results.size(); i++) {
				startTime = System.currentTimeMillis();
				writer.println(ResultReaderWriter.dominatingSetToString(results.get(i)));
				writer.flush();
				System.out.println(new Integer(i + 1).toString() + "/" + results.size() + " placed: "
						+ new Integer(((int) ((System.currentTimeMillis() - startTime) / 1000))).toString() + "s");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeResultToFile(Set<Integer> result, String filePathToWrite) {
		System.out.println("Printing result to: " + filePathToWrite + " file");
		try {
			long startTime = System.currentTimeMillis();

			PrintWriter writer = new PrintWriter(filePathToWrite);
			writer.println(ResultReaderWriter.dominatingSetToString(result));
			writer.close();

			System.out.println("Result placed in " + filePathToWrite + " file: "
					+ new Integer(((int) ((System.currentTimeMillis() - startTime) / 1000))).toString() + "s");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Set<Integer>> readResultsFromFile(String filePathToRead) {
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();

		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(filePathToRead));
			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			Set<Integer> dominatingSet;
			while ((line = br.readLine()) != null) {
				dominatingSet = new HashSet<Integer>();
				String[] vertexArray = line.split(",");

				for (int i = 0; i < vertexArray.length; i++) {
					dominatingSet.add(Integer.parseInt(vertexArray[i]));
				}
				result.add(dominatingSet);
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static String dominatingSetToString(Set<Integer> domSet) {
		String result = "";

		Iterator<Integer> iterator = domSet.iterator();

		while (iterator.hasNext()) {
			result += "," + iterator.next();
		}

		return result.substring(1);
	}
}
