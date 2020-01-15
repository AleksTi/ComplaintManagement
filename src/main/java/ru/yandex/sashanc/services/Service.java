package ru.yandex.sashanc.services;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class Service {
    private static final Logger logger = Logger.getLogger(Main.class);

    public void createComplaintDocs(String fileNameSap, String fileName1C) {
        getDataSap(fileNameSap);
    }

    public void getDataSap(String fileNameSap) {
        try (XSSFWorkbook workbookSap = new XSSFWorkbook(new FileInputStream(fileNameSap))) {
            XSSFSheet sheetA = workbookSap.getSheetAt(0);
            Iterator rowIter = sheetA.rowIterator();
            Object cellValue = "";
            while (rowIter.hasNext()) {
                XSSFRow rowA = (XSSFRow) rowIter.next();
                Iterator cellIter = rowA.cellIterator();
                while (cellIter.hasNext()) {
                    XSSFCell cellA = (XSSFCell) cellIter.next();
                    if (cellA != null) {
                        switch (cellA.getCellType()) {
                            case _NONE:
                                cellValue = "none";
                                break;
                            case NUMERIC:
                                cellValue = (int)cellA.getNumericCellValue();
                                break;
                            case STRING:
                                cellValue = cellA.getStringCellValue();
                                break;
//                            case FORMULA:
//                                break;
                            case BLANK:
                                cellValue = "blank";
                                break;
//                            case BOOLEAN:
//                                break;
//                            case ERROR:
//                                break;
                        }
                    }

                    logger.info(rowA.getRowNum() + " " + cellA.getColumnIndex() + "  " + cellValue);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
