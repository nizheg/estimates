package org.nzhegalin.estimate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.nzhegalin.estimate.entity.Report;
import org.nzhegalin.estimate.entity.ReportRow;
import org.nzhegalin.estimate.entity.Resource;

public class ExcelReportGenerator {

	private final int DESCRIPTION_COL_INDEX = 1;
	private final int MEASURE_UNIT_COL_INDEX = 2;
	private final int CODE_COL_INDEX = 3;
	private final int ACTUAL_MEASURE_COL_INDEX = 4;
	private final int FIRST_MATERIAL_COL_INDEX = ACTUAL_MEASURE_COL_INDEX + 1;

	private final int HEADER_FIRST_ROW = 1;
	private final int HEADER_SECOND_ROW = HEADER_FIRST_ROW + 1;
	private final int HEADER_THIRD_ROW = HEADER_SECOND_ROW + 1;
	private final int BODY_FIRST_ROW = HEADER_THIRD_ROW + 1;

	private final Report sourceReport;
	Workbook wb;
	private Sheet sheet;
	private CellStyle style;

	Map<Resource, Integer> resourceIndices = new TreeMap<Resource, Integer>();

	public ExcelReportGenerator(Report report) {
		sourceReport = report;
	}

	public InputStream generate() {
		try {
			initializeTable();
			createHeader();
			int currentRow = BODY_FIRST_ROW;
			for (ReportRow reportRow : sourceReport.getRows()) {
				Row row = sheet.createRow(currentRow);
				Cell cell = row.createCell(DESCRIPTION_COL_INDEX);
				cell.setCellValue(reportRow.getName());
				cell = row.createCell(MEASURE_UNIT_COL_INDEX);
				cell.setCellValue(reportRow.getMeasureUnit());
				cell = row.createCell(CODE_COL_INDEX);
				cell.setCellValue(reportRow.getCode());
				cell = row.createCell(ACTUAL_MEASURE_COL_INDEX);
				cell.setCellValue(reportRow.getActualMeasure());
				for (Resource resource : reportRow.getResources()) {
					createResourceTableItem(row, resource, reportRow);
				}
				currentRow++;
			}

			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			return new ByteArrayInputStream(fileOut.toByteArray());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void createResourceTableItem(Row row, Resource resource,
			ReportRow reportRow) {
		int position = resourceIndices.get(resource);
		Cell cell = row.createCell(position);
		Double measure = reportRow.getResourceMeasure(resource);
		cell.setCellValue(measure);
		cell = row.createCell(position + 1);
		Double actualMeasure = reportRow.getReourceActualMeasure(resource);
		cell.setCellValue(actualMeasure);
	}

	private void initializeTable() {
		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Smeta");

		style = wb.createCellStyle();
		style.setWrapText(true);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		sheet.setColumnWidth(DESCRIPTION_COL_INDEX, 100 * 256);
		sheet.setColumnWidth(MEASURE_UNIT_COL_INDEX, 28 * 256);
		sheet.setColumnWidth(CODE_COL_INDEX, 35 * 256);
		sheet.setColumnWidth(ACTUAL_MEASURE_COL_INDEX, 22 * 256);
		sheet.setDefaultColumnWidth(15);
	}

	private void createHeader() {
		createHeaderRows();
		createConstantPartOfTableHeader();
		int currentMaterialsColumn = FIRST_MATERIAL_COL_INDEX;
		for (Resource resource : sourceReport.getResources()) {
			currentMaterialsColumn = createMaterialBlock(
					currentMaterialsColumn, resource);
		}

	}

	private void createConstantPartOfTableHeader() {
		Row firstRowOfHeader = sheet.getRow(HEADER_FIRST_ROW);

		Cell cell = firstRowOfHeader.createCell(DESCRIPTION_COL_INDEX);
		cell.setCellValue("Наименование объектов и видов работ");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_FIRST_ROW,
				HEADER_THIRD_ROW, DESCRIPTION_COL_INDEX, DESCRIPTION_COL_INDEX));

		cell = firstRowOfHeader.createCell(MEASURE_UNIT_COL_INDEX);
		cell.setCellValue("Единица измерения работ");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_FIRST_ROW,
				HEADER_THIRD_ROW, MEASURE_UNIT_COL_INDEX,
				MEASURE_UNIT_COL_INDEX));

		cell = firstRowOfHeader.createCell(CODE_COL_INDEX);
		cell.setCellValue("Номер единичных расценок");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_FIRST_ROW,
				HEADER_THIRD_ROW, CODE_COL_INDEX, CODE_COL_INDEX));

		cell = firstRowOfHeader.createCell(ACTUAL_MEASURE_COL_INDEX);
		cell.setCellValue("Количество фактически выполненных работ");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_FIRST_ROW,
				HEADER_THIRD_ROW, ACTUAL_MEASURE_COL_INDEX,
				ACTUAL_MEASURE_COL_INDEX));
	}

	private void createHeaderRows() {
		sheet.createRow(HEADER_FIRST_ROW);
		sheet.createRow(HEADER_SECOND_ROW);
		sheet.createRow(HEADER_THIRD_ROW);
	}

	private int createMaterialBlock(int position, Resource resource) {
		Row row = sheet.getRow(HEADER_FIRST_ROW);
		Cell cell = row.createCell(position);
		cell.setCellValue(resource.getCode());
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_FIRST_ROW,
				HEADER_FIRST_ROW, position, position + 1));

		Row row2 = sheet.getRow(HEADER_SECOND_ROW);
		cell = row2.createCell(position);
		cell.setCellValue(resource.getName());
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(HEADER_SECOND_ROW,
				HEADER_SECOND_ROW, position, position + 1));

		Row row3 = sheet.getRow(HEADER_THIRD_ROW);

		cell = row3.createCell(position);
		cell.setCellValue("норма на единицу работ");
		cell.setCellStyle(style);

		cell = row3.createCell(position + 1);
		cell.setCellValue("на выполненный объем работ");
		cell.setCellStyle(style);
		resourceIndices.put(resource, position);
		return position + 2;
	}

}
