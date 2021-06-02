package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	/**
	 * This method is used to get the contents of a cell
	 * 
	 * @param cell - reference to the desired cell
	 * @return String containing the cell contents
	 */
	private static String getCellContents(Cell cell) {
		String cellContent;
		switch (cell.getCellType()) {
		case BOOLEAN:
			cellContent = cell.getBooleanCellValue() + "";
			break;
		case NUMERIC:
			cellContent = cell.getNumericCellValue() + "";
			break;
		case STRING:
			cellContent = cell.getStringCellValue();
			break;
		case _NONE:
		case BLANK:
		default:
			cellContent = "";
		}
		return cellContent;
	}

	/**
	 * This method is used to read the data from all the cells of the given sheet in
	 * the given workbook. The only requirement is that the 1st row of the excel
	 * sheet should be the header row containing the column names
	 * 
	 * @param filePath  - path to the excel workbook
	 * @param sheetName - name of the worksheet in the given workbook
	 * @return List of Map - Each item in the List represents a row. Each rows' data
	 *         is captured in form of a Map with column name's as Map's key and
	 *         cell's value as Map's value
	 */
	public static List<Map<String, String>> read(String filePath, String sheetName) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet != null) {
				Iterator<Row> rowIter = sheet.rowIterator();
				Row headRow = null;
				while (rowIter.hasNext()) {
					Row row = rowIter.next();
					if (row.getRowNum() == 0)
						headRow = row;
					else {
						Map<String, String> currentRowData = new LinkedHashMap<String, String>();
						Iterator<Cell> cellIter = row.cellIterator();
						while (cellIter.hasNext()) {
							Cell cell = cellIter.next();
							currentRowData.put(getCellContents(headRow.getCell(cell.getColumnIndex())),
									getCellContents(cell));
						}
						data.add(currentRowData);
					}
				}
				fis.close();
				workbook.close();
			} else {
				fis.close();
				workbook.close();
				throw new Exception("There is no sheet named \"" + sheetName + "\"");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return data;
	}

	/**
	 * This method is used to read the data from all the cells of the given row in
	 * the given sheet of the given workbook. The only requirement is that the 1st
	 * row of the excel sheet should be the header row containing the column names
	 * 
	 * @param filePath  - path to the excel workbook
	 * @param sheetName - name of the worksheet in the given workbook
	 * @param row       - row number whose data is desired
	 * @return row data in form of a Map. The Map's keys are column names and Map's
	 *         values are cells values of the desired row
	 */
	public static Map<String, String> read(String filePath, String sheetName, int row) {
		List<Map<String, String>> sheetData = read(filePath, sheetName);
		try {
			if (sheetData.size() == 0)
				throw new Exception("No data is present in the sheet");
			else if (row < 1 || row > sheetData.size())
				throw new Exception("Invalid row number: " + row + ". Valid row range is [1," + sheetData.size() + "]");
			else
				return sheetData.get(row - 1);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * This method is used to read the data from the given column in the given row
	 * in the given sheet of the given workbook. The only requirement is that the
	 * 1st row of the excel sheet should be the header row containing the column
	 * names
	 * 
	 * @param filePath  - path to the excel workbook
	 * @param sheetName - name of the worksheet in the given workbook
	 * @param row       - row number
	 * @param column    - column Name of the cell whose contents are desired
	 * @return String containing the cell data
	 */
	public static String read(String filePath, String sheetName, int row, String column) {
		Map<String, String> rowData = read(filePath, sheetName, row);
		try {
			if (!rowData.containsKey(column))
				throw new Exception("Invalid column name - " + column);
			else
				return rowData.get(column);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * This method is used to write dta in the given cell of the given row of the
	 * giben sheet of the given workbook
	 * 
	 * @param filePath    - path to the excel workbook
	 * @param sheetName   - name of the worksheet in the given workbook
	 * @param content     - data to be written to the cell
	 * @param rowIndex    - 0-based row Index
	 * @param columnIndex - 0-based cell Index
	 */
	public static void write(String filePath, String sheetName, String content, int rowIndex, int columnIndex) {
		File file = new File(filePath);
		if (!file.exists()) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			Row row = sheet.createRow(rowIndex);
			Cell cell = row.createCell(columnIndex);
			cell.setCellValue(content);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				workbook.write(fos);
				fos.close();
				workbook.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} else {
			try {
				FileInputStream fis = new FileInputStream(file);
				Workbook workbook = WorkbookFactory.create(fis);
				fis.close();
				Sheet sheet = workbook.getSheet(sheetName);
				if (sheet == null)
					sheet = workbook.createSheet(sheetName);
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					row = sheet.createRow(rowIndex);
				Cell cell = row.createCell(columnIndex);
				if (cell == null)
					cell = row.createCell(columnIndex);
				cell.setCellValue(content);
				FileOutputStream fos = new FileOutputStream(file);
				workbook.write(fos);
				fos.close();
				workbook.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		write("C:\\Users\\91770\\Desktop\\Random Challenges\\New folder\\joda.xlsx", "germanium", "kaku", 10, 15);
	}
}
