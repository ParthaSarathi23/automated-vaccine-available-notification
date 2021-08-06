package com.learn.httpclient.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.learn.httpclient.dto.VaccineCenterVO;
import com.learn.httpclient.service.UplodDataService;

@Service
public class UploadDataServiceImpl implements UplodDataService {

	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(HttpClientServiceImpl.class);
	private static final String[] excelColumnName = { "centerName", "pincode", "blockName", "availableCapacity",
			"availableDate" };

	@Override
	public Map<String, Object> writeDataToExcelSheet(List<VaccineCenterVO> vaccineCenterList) {
		LOGGER.info("Excel sheet creation started " + Calendar.getInstance().getTime());
		Map<String, Object> serviceMap = new HashMap<String, Object>();
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("VACCINE_CENTER_LIST");
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < excelColumnName.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(excelColumnName[i]);
		}
		int rowNumber = 1;
		for (VaccineCenterVO vaccineVO : vaccineCenterList) {
			Row sheetRow = sheet.createRow(rowNumber++);
			sheetRow.createCell(0).setCellValue(vaccineVO.getCenterName());
			sheetRow.createCell(1).setCellValue(vaccineVO.getPincode());
			sheetRow.createCell(2).setCellValue(vaccineVO.getBlockName());
			sheetRow.createCell(3).setCellValue(vaccineVO.getAvailableCapacity());
			sheetRow.createCell(4).setCellValue(vaccineVO.getAvailableDate());
		}
		try {
			FileOutputStream createdFile = new FileOutputStream("vaccineCenter.xlsx");
			workBook.write(createdFile);
			createdFile.close();
			workBook.close();
			LOGGER.info("Excel sheet creation finished " + Calendar.getInstance().getTime());
		} catch (IOException e) {
			e.printStackTrace();
		}
		serviceMap.put("STATUS", "200");
		return serviceMap;
	}

}
