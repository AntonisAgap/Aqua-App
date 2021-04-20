package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadXLSX {
    public static void main(String[] args) throws IOException {
        File excelFile = new File("C:\\Users\\green\\IdeaProjects\\nativeApp\\src\\main\\csvFiles\\a++ order sheet.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(6);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();
        DataFormatter dataFormatter = new DataFormatter();

        Row initRow = sheet.getRow(1);
        Iterator<Cell> initcellIterator = initRow.cellIterator();
        int quantityCell=6;
        int count = 1;
        while (initcellIterator.hasNext()) {
            Cell cell = initcellIterator.next();
            if (cell.toString().contains("ORDER Q'TY")) {
                System.out.println("Found!"+cell.toString());
                System.out.println(count);
                quantityCell = count+1;
                break;
            }
            count = count+1;
        }

        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            try {
                String productID = row.getCell(0).getStringCellValue();
                String firstLetter = String.valueOf(productID.charAt(0));
                if (firstLetter.equals("0")) {
                    System.out.println(row.getCell(1).getStringCellValue()+" : "+
                            row.getCell(quantityCell).getNumericCellValue());
                }
                Iterator<Cell> cellIterator = row.cellIterator();
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    System.out.print(cell.toString() + ";");
////                }

            } catch (Exception e) {
            }
//
            System.out.println();
//        }

            workbook.close();
            fis.close();
        }
    }
}
