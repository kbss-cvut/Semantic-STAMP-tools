/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.data.audit.xls;

import cz.cvut.kbss.commons.utils.PoiUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public abstract class XLSXIterator {
    private static final Logger LOG = LoggerFactory.getLogger(XLSXIterator.class);

    protected Workbook myWorkBook;
    protected Sheet currentSheet;
    protected Table currentTable;
    
    protected List<Table> tables = new ArrayList<>();
  
    
    /**
     * Parses the is stream and reads the reports in it as AuditReports
     * @param is a stream of the safa xlsx containing numerous safa reports
     * @throws IOException 
     */
    public void process(final InputStream is) throws IOException {
        // init
        myWorkBook = new XSSFWorkbook(is);
        for(int i = 0; i < myWorkBook.getNumberOfSheets(); i++){
            LOG.info("Processing Sheet ({}) - {}", i, myWorkBook.getSheetName(i));
            processSheet(myWorkBook.getSheetAt(i));
        }
    }

    protected void processSheet(Sheet s) {
        currentSheet = s;
        currentTable = getTable(currentSheet);
        if(currentTable == null || currentTable.getRowProcessor() == null){
            return;
        }
        
        System.out.println("processing " + currentTable + " table...");
        Iterator<Row> iter = s.rowIterator();
        while (iter.hasNext()) {
            final Row row = iter.next();
            processRow(row);
        }
    }
    
    protected void processRow(Row row) {
        if(row == null || row.getRowNum() == 0){
            LOG.info("Processing ROW {}", row.getRowNum());
            return;
        }

        if(ignoreRow(row))
            return;
        
        if(currentTable.getRowProcessor() != null)
            currentTable.getRowProcessor().accept(row);
    }
    
    protected Table getTable(Sheet s){
        int sheetIndex = myWorkBook.getSheetIndex(s);
        return tables.stream().
                filter(t -> t.getSheetIndex() == sheetIndex).
                findFirst().
                orElse(null);
    }
    
    public boolean ignoreRow(Row row){
        return PoiUtils.isEmpty(getCell(row, currentTable.getNonNullCollum()));
    }
    
    public Cell getCell(Row r, Enum e){
        return r.getCell(e.ordinal());
    }    
    
    public String getStringValue(Row r, Enum e){
        Cell c = getCell(r, e);
        return PoiUtils.getAsString(c).trim();
    }
    
    
    protected Table addTable(String tableName, int sheetIndex, Enum nonNullColumn, Consumer<Row> rowProcessor){
        Table t = new Table(tableName, sheetIndex, nonNullColumn, rowProcessor);
        tables.add(t);
        return t;
    }
    
    
    protected static class Table{
        String tableName;
        Enum nonNullCollum;
        int sheetIndex;
        Consumer<Row> rowProcessor;

        public Table(String tableName, int sheetIndex, Enum nonNullCollum, Consumer<Row> rowProcessor) {
            this.tableName = tableName;
            this.nonNullCollum = nonNullCollum;
            this.sheetIndex = sheetIndex;
            this.rowProcessor = rowProcessor;
        }


        public String getTableName() {
            return tableName;
        }

        public Enum getNonNullCollum() {
            return nonNullCollum;
        }

        public int getSheetIndex() {
            return sheetIndex;
        }

        public Consumer<Row> getRowProcessor() {
            return rowProcessor;
        }

    }

}
