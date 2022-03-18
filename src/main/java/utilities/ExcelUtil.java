package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelUtil {

    private static final ArrayList <String> headerList = new ArrayList <String> ();
    private static final ArrayList <String> testdataList = new ArrayList <String> ();

    public static synchronized void readExcel(String scenarioName)throws IOException
	{
    	System.out.println("Read Excecl for case: "+scenarioName);
    	XSSFWorkbook excelWBook;
        XSSFSheet excelWSheet;

        DataFormatter dataFormatter = new DataFormatter();
        String path = "resource/AIAPartner_Admin_Portal.xlsx";

        try {
            FileInputStream excelFile = new FileInputStream(path);
            excelWBook = new XSSFWorkbook(excelFile);
            excelWSheet = excelWBook.getSheetAt(0);
            headerList.clear();
            testdataList.clear();

            for (Row row: excelWSheet) {
                String cellValue1stCol = dataFormatter.formatCellValue(row.getCell(0));
                String cellValue2stCol = dataFormatter.formatCellValue(row.getCell(1));

                if (cellValue1stCol.trim().equals("TestCaseID")) {
                    for (Cell cell: row) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        headerList.add(cellValue);
                    }
                }
                //Setup the array list for test data
                else if (cellValue1stCol.trim().equals(scenarioName.trim())||cellValue2stCol.trim().equals(scenarioName.trim())) {
                    for (Cell cell: row) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        testdataList.add(cellValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int hlsize = headerList.size();
        int dlsize = testdataList.size();
        System.out.println("hlsize:"+hlsize);
        System.out.println("dlsize:"+dlsize);
	}
    
    public static String getValue(String fieldName) {
    	int ColIndex = headerList.indexOf(fieldName.trim());
        return testdataList.get(ColIndex).trim();
    }

    public static synchronized void setValue(String fieldName, String value) {
        int ColIndex = headerList.indexOf(fieldName.trim());
        testdataList.set(ColIndex, value);
    }
}
