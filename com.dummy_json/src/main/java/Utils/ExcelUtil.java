package Utils;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {

    public static Map<String, String> getData(String testCaseID) {

        Map<String, String> map = new HashMap<>();

        try {
            InputStream fis = ExcelUtil.class
                    .getClassLoader()
                    .getResourceAsStream("hell.xlsx");

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            XSSFRow header = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                XSSFRow row = sheet.getRow(i);

                if (row.getCell(0).toString().trim().equals(testCaseID)) {

                    for (int j = 0; j < header.getLastCellNum(); j++) {

                        String rawKey = header.getCell(j).toString();

                        String key = rawKey.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

                        String value = "";

                        if (row.getCell(j) != null) {
                            row.getCell(j).setCellType(CellType.STRING);
                            value = row.getCell(j).getStringCellValue().trim();
                        }

                        map.put(key, value);
                    }
                }
            }

            wb.close();

        } catch (Exception e) {
            throw new RuntimeException("Excel error: " + e.getMessage());
        }

        return map;
    }
}