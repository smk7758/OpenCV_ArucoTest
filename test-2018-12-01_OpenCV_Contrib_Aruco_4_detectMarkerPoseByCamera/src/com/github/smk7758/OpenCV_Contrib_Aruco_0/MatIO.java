package com.github.smk7758.OpenCV_Contrib_Aruco_0;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MatIO {
	public static Map<String, Mat> loadMat(Path filePath) {
		Document document = getDocument(filePath);
		Element root = document.getDocumentElement();

		NodeList rootNodeList = root.getChildNodes();
		Map<String, Mat> mats = new HashMap<>();
		for (int i = 0; i < rootNodeList.getLength(); i++) {
			if (rootNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				mats.put(rootNodeList.item(i).getNodeName(), getMatElement(root, rootNodeList.item(i).getNodeName()));
			}
		}

		return mats;
	}

	private static Mat getMatElement(Element root, String matName) {
		NodeList matDataRoot = root.getElementsByTagName(matName);
		Element matDataElement = (Element) matDataRoot.item(0);
		final int rows = Integer.valueOf(matDataElement.getAttribute("rows"));
		final int cols = Integer.valueOf(matDataElement.getAttribute("cols"));
		final int channels = Integer.valueOf(matDataElement.getAttribute("channels"));
		final int dims = Integer.valueOf(matDataElement.getAttribute("dims")); // TODO
		String matDataString = matDataElement.getTextContent();
		matDataString = matDataString.replaceAll("\\[", "").replaceAll("\\]", "").trim();

		String[] matDataStringLines = matDataString.split(";");

		Mat mat = new Mat(rows, cols, CvType.CV_32FC(channels));
		for (int row = 0; row < rows; row++) {
			String[] matDataSplitted = matDataStringLines[row].split(",");

			for (int col = 0; col < cols; col++) {
				float[] matData = new float[channels];
				for (int channel = 0; channel < channels; channel++) {
					System.out.println("matDataSplitted: " + matDataSplitted[col * channels + channel].trim());

					matData[channel] = Float.valueOf(matDataSplitted[col * channels + channel].trim());

					System.out.println("matData[channel]: " + matData[channel]);
				}

				mat.put(row, col, matData);
			}
		}

		return mat;
	}

	private static Document getDocument(Path filePath) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(filePath.toString());
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return document;
	}

	public static void exportMat(Map<String, Mat> mats, Path filePath) {
		try {
			Document document = getDocument();

			Element root = document.createElement("root");
			document.appendChild(root);

			for (Entry<String, Mat> entryMat : mats.entrySet()) {
				final Element matData = setMatElement(document, entryMat.getKey(), entryMat.getValue());
				root.appendChild(matData);
			}

			BufferedWriter bw = Files.newBufferedWriter(filePath);
			outputDocument(document, new StreamResult(bw));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static Element setMatElement(Document document, String matName, Mat mat) {
		Element matData = document.createElement(matName);
		matData.setAttribute("rows", String.valueOf(mat.rows()));
		matData.setAttribute("cols", String.valueOf(mat.cols()));
		matData.setAttribute("channels", String.valueOf(mat.channels()));
		matData.setAttribute("dims", String.valueOf(mat.dims()));
		matData.setTextContent(mat.dump());
		return matData;
	}

	private static Document getDocument() {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = null;
		try {
			docbuilder = dbfactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		}
		return docbuilder.newDocument();
	}

	private static void outputDocument(Document document, StreamResult streamResult) {
		try {
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			// transformer.setOutputProperty("method", "html"); //宣言無し
			transformer.setOutputProperty("indent", "yes"); // 改行指定
			transformer.setOutputProperty("encoding", "SHIFT_JIS"); // encoding

			transformer.transform(new DOMSource(document), streamResult);
		} catch (TransformerConfigurationException ex) {
			ex.printStackTrace();
		} catch (TransformerException ex) {
			ex.printStackTrace();
		}
	}

}
