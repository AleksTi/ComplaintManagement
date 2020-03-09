package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import ru.yandex.sashanc.pojo.LetterInfoNot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LetterInfoDaoImpl implements ILetterInfoDao {
    private static final Logger logger = Logger.getLogger(LetterInfoDaoImpl.class);

    @Override
    public void createLetter(List<LetterInfoNot> notList1, List<LetterInfoNot> notList2, Path path){
        InputStream letterBlank = getClass().getResourceAsStream("/blanks/LetterInfo.docx");
        try (XWPFDocument doc = new XWPFDocument(letterBlank)) {
            XWPFTable table1 = doc.getTableArray(1);
            XWPFTable table2 = doc.getTableArray(2);
            int qnt = inputDataTable(notList1, table1);
            int qnt1C = inputDataTable(notList2, table2);
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null){
                            if (text.contains("q1C")) {
                                text = text.replace("q1C", Integer.toString(qnt1C));
                            }
                            if (text.contains("qnt")) {
                                text = text.replace("qnt", Integer.toString(qnt));
                            }
                        }
                        r.setText(text, 0);
                    }
                }
            }
            path = Paths.get(path.toString() + "\\" + "Письмо вызов.docx");
            FileOutputStream out = new FileOutputStream(path.toFile());
            doc.write(out);
        } catch (IOException e) {
            logger.info(e);
        }
    }

    private int inputDataTable(List<LetterInfoNot> notList, XWPFTable table){
        int id = 1;
        int qnt = 0;
        XWPFTableRow row;
        for (LetterInfoNot letterInfoNot : notList){
            row = table.createRow();
            row.getCell(0).setText(Integer.toString(id++));
            row.getCell(1).setText(letterInfoNot.getPartNumber());
            row.getCell(2).setText(letterInfoNot.getNotNumberList().toString());
            row.getCell(3).setText(letterInfoNot.getPartDescriptionSap());
            row.getCell(4).setText(letterInfoNot.getDeviationDescriptionSap());
            row.getCell(5).setText(Integer.toString(letterInfoNot.getQuantity()));
            qnt += letterInfoNot.getQuantity();
        }
        return qnt;
    }
}
