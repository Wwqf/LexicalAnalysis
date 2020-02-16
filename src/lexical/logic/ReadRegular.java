package lexical.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lexical.algorithm.Thompson;
import lexical.structure.Production;
import lexical.structure.TransitionGraph;
import log.IOColor;

import java.io.*;
import java.util.*;

public class ReadRegular {

	public String inputPath;
	public String outputPath;
	public String samplePath;

	private JSONObject sampleJson;
	private List<Production> productions;
	private Map<String, TransitionGraph> transitionGraphs = new LinkedHashMap<>();

	public ReadRegular(String sampleFilename) {

		/*
		 * 初始化文件路径
		 * 包括输入文件，输出文件，以及样例文件
		 */
		File file = new File("");

		String projectPath = "";
		try {
			projectPath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		inputPath = projectPath + "/src/data/input/";
		outputPath = projectPath + "/src/data/output/";
		samplePath = projectPath + "/src/data/sample/" + sampleFilename;

		IOColor.GREEN.println("Tip: Make sure the /src/data/[input/ output/ sample/] path exists.");
	}

	/**
	 * 读取样例文件
	 * @return this
	 */
	private ReadRegular readSampleFile() {
		File file = new File(samplePath);
		try {
			InputStream stream = new FileInputStream(file);
			int avail = stream.available();
			byte[] bytes = new byte[avail];
			stream.read(bytes);
			sampleJson = JSON.parseObject(new String(bytes));
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 读取输入文件
	 * @return this
	 */
	private ReadRegular readInputFile() {
		productions = new ArrayList<>();
		JSONArray template = sampleJson.getJSONArray("template");
		GrammarFile grammarFile = new GrammarFile();

		for (Object filename : template) {
			productions.addAll(grammarFile.read(inputPath + filename).getGrammarList());
		}
		return this;
	}

	/**
	 * 生成有限状态自动机
	 * @return this
	 */
	public ReadRegular generateDiagram() {
		readSampleFile();
		readInputFile();

		for (Production production : productions) {
			Thompson thompson = new Thompson(transitionGraphs, production);
			TransitionGraph graph = thompson.execute();
			transitionGraphs.put(production.getHead(), graph);
		}
		return this;
	}

	public Map<String, TransitionGraph> getTransitionGraphs() {
		return transitionGraphs;
	}

	public List<String> getTestcaseFilename() {
		List<String> result = new ArrayList<>();
		JSONArray template = sampleJson.getJSONArray("testcase");
		for (Object item : template) {
			result.add(inputPath + item);
		}
		return result;
	}

	/* 读取产生式集合，即一个输入文件 */
	public static final class GrammarFile {
		private BufferedReader reader;
		private List<Production> grammarList = new ArrayList<>();;

		public GrammarFile read(String filePath) {
			File file = new File(filePath);

			if (!file.exists()) {
				System.out.println("The file was not found.");
				System.exit(1);
			}

			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return this;
		}

		public List<Production> getGrammarList() {
			String production;

			try {
				while ((production = reader.readLine()) != null) {
					if (production.trim().equals("")) continue;

					String[] strings = production.split("->");
					if (strings.length != 2) {
						System.out.println("The production rule has wrong.");
						System.exit(1);
					}
					Production grammar = new Production(strings[0].trim(), strings[1].trim());
					grammarList.add(grammar);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return grammarList;
		}
	}
}
