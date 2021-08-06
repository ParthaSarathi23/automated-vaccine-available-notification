package com.learn.httpclient.utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.poi.ss.usermodel.Workbook;

import com.learn.httpclient.Exception.CustomRuntimeException;
import com.monitorjbl.xlsx.StreamingReader;

public class ExcelUtility {

	private static ExcelUtility instance;
	private static final String INVALID_EXCEL_PATH_ERROR = "Invalid path provided for excel";

	public static ExcelUtility getInstance() {
		if (instance == null) {
			return instance = new ExcelUtility();
		} else {
			return instance;
		}
	}

	public List<String> getWorkBookSheetNames(byte[] buf) {
		if (buf == null || buf.length == 0) {
			throw new CustomRuntimeException(INVALID_EXCEL_PATH_ERROR);
		}
		try (InputStream is = new ByteArrayInputStream(buf);
				Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is)) {
			int numberOfSheets = workbook.getNumberOfSheets();
			List<String> sheetNameList = new ArrayList<>();
			for (int i = 0; i < numberOfSheets; i++) {
				sheetNameList.add(workbook.getSheetName(i));
			}
			return sheetNameList;
		} catch (Exception e) {
			throw new CustomRuntimeException(e);
		}
	}

}
