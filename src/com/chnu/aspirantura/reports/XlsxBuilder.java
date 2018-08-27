package com.chnu.aspirantura.reports;

import com.enterprisemath.utils.ValidationUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel report builder.
 *
 * @author radek.hecl
 */
public class XlsxBuilder {
    /**
     * Underlined workbook.
     */
    private Workbook workbook;
    /**
     * Current sheet.
     */
    private Sheet sheet = null;
    /**
     * Current row.
     */
    private Row row = null;
    /**
     * Next row index.
     */
    private int nextRowIdx = 0;
    /**
     * Style attributes for row.
     */
    private Set<StyleAttribute> rowStyleAttributes;
    /**
     * Next column index.
     */
    private int nextColumnIdx = 0;
    private Map<Set<StyleAttribute>, CellStyle> styleBank = new HashMap<Set<StyleAttribute>, CellStyle>();

    /**
     * Creates new instance.
     */
    public XlsxBuilder() {
        workbook = new XSSFWorkbook();
    }

    /**
     * Starts sheet.
     *
     * @param name sheet name
     * @return this instance
     */
    public XlsxBuilder startSheet(String name) {
        sheet = workbook.createSheet(name);
        nextRowIdx = 0;
        nextColumnIdx = 0;
        rowStyleAttributes = new HashSet<StyleAttribute>();
        return this;
    }

    /**
     * Sets auto sizing columns.
     *
     * @param idx column index, starting from 0
     * @return this instance
     */
    public XlsxBuilder setAutoSizeColumn(int idx) {
        sheet.autoSizeColumn(idx);
        return this;
    }

    public XlsxBuilder setAutoSizeAllColumns() {
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
        return this;
    }

    /**
     * Sets column size.
     *
     * @param idx column index, starting from 0
     * @param m   number of 'M' standard characters to use for size calculation
     * @return this instance
     */
    public XlsxBuilder setColumnSize(int idx, int m) {
        sheet.setColumnWidth(idx, (m + 1) * 256);
        return this;
    }

    /**
     * Starts new row.
     *
     * @return this instance
     */
    public XlsxBuilder startRow() {
        row = sheet.createRow(nextRowIdx);
        nextRowIdx = nextRowIdx + 1;
        nextColumnIdx = 0;
        rowStyleAttributes = new HashSet<StyleAttribute>();
        return this;
    }

    /**
     * Sets row top border as thin.
     *
     * @return this instance
     */
    public XlsxBuilder setRowThinTopBorder() {
        ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
        row.setRowStyle(getCellStyle(StyleAttribute.THIN_TOP_BORDER));
        rowStyleAttributes.add(StyleAttribute.THIN_TOP_BORDER);
        return this;
    }

    /**
     * Sets row top border as thick.
     *
     * @return this instance
     */
    public XlsxBuilder setRowThickTopBorder() {
        ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
        row.setRowStyle(getCellStyle(StyleAttribute.THICK_TOP_BORDER));
        rowStyleAttributes.add(StyleAttribute.THICK_TOP_BORDER);
        return this;
    }

    /**
     * Sets row bottom border as thin.
     *
     * @return this instance
     */
    public XlsxBuilder setRowThinBottomBorder() {
        ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
        row.setRowStyle(getCellStyle(StyleAttribute.THIN_BOTTOM_BORDER));
        rowStyleAttributes.add(StyleAttribute.THIN_BOTTOM_BORDER);
        return this;
    }

    /**
     * Sets row bottom border as thick.
     *
     * @return this instance
     */
    public XlsxBuilder setRowThickBottomBorder() {
        ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
        row.setRowStyle(getCellStyle(StyleAttribute.THICK_BOTTOM_BORDER));
        rowStyleAttributes.add(StyleAttribute.THICK_BOTTOM_BORDER);
        return this;
    }

    /**
     * Sets row height to capture the title.
     *
     * @return this instance
     */
    public XlsxBuilder setRowTitleHeight() {
        ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
        row.setHeightInPoints(30);
        return this;
    }

    /**
     * Adds title column.
     *
     * @param text text
     * @return this instance
     */
    public XlsxBuilder addTitleTextColumn(String text) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.TITLE_SIZE, StyleAttribute.BOLD);
        cell.setCellStyle(style);
        cell.setCellValue(StringUtils.stripToEmpty(text));
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }

    /**
     * Adds simple left aligned text.
     *
     * @param text text
     * @return this instance
     */
    public XlsxBuilder addTextLeftAlignedColumn(String text) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_LEFT);
        cell.setCellStyle(style);
        cell.setCellValue(StringUtils.stripToEmpty(text));
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }


    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Adds simple center aligned text.
     *
     * @param text text
     * @return this instance
     */
    public XlsxBuilder addTextCenterAlignedColumn(String text) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cell.setCellStyle(style);



        try{
            cell.setCellValue(Integer.parseInt(text));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        }catch (Exception e){
            cell.setCellValue(StringUtils.stripToEmpty(text));
        }
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }

    /**
     * Adds simple center aligned text.
     *
     * @param val value
     * @return this instance
     */
    public XlsxBuilder addDoubleCenterAlignedColumn(double val) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_CENTER);
        cell.setCellStyle(style);
        cell.setCellValue(val);
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }

    public XlsxBuilder addTitleToColumn(int regionId, String text) {
        CellRangeAddress region = sheet.getMergedRegion(regionId);

        int colIndex = region.getFirstColumn();
        int rowNum = region.getFirstRow();

        Cell cell = sheet.getRow(rowNum).getCell(colIndex);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_CENTER, StyleAttribute.BOLD);
        style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        style.setWrapText(true);
        cell.setCellStyle(style);
        if (regionId==0) sheet.setColumnWidth(cell.getColumnIndex(), (text.length()) * 256);
        else if(regionId==1) sheet.setColumnWidth(cell.getColumnIndex(), (8) * 256);
        else if(regionId==2) sheet.setColumnWidth(cell.getColumnIndex(), (6) * 256);
        else if(regionId==3) sheet.setColumnWidth(cell.getColumnIndex(), ((text.length())* 256));
        else if(regionId==4) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==5) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==6) sheet.setColumnWidth(cell.getColumnIndex(), ((text.length()/2-7) * 256));
        else if(regionId==7) sheet.setColumnWidth(cell.getColumnIndex(), (9) * 256);
        else if(regionId==8) sheet.setColumnWidth(cell.getColumnIndex(), (9) * 256);
        else if(regionId==9) sheet.setColumnWidth(cell.getColumnIndex(), (9) * 256);
        else if(regionId==10) sheet.setColumnWidth(cell.getColumnIndex(), (9) * 256);
        else if(regionId==11) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==12) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==13) sheet.setColumnWidth(cell.getColumnIndex(), (22) * 256);
        else if(regionId==14) sheet.setColumnWidth(cell.getColumnIndex(), (11) * 256);
        else if(regionId==15) sheet.setColumnWidth(cell.getColumnIndex(), (11) * 256);
        else if(regionId==16) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==17) sheet.setColumnWidth(cell.getColumnIndex(), (10) * 256);
        else if(regionId==18) sheet.setColumnWidth(cell.getColumnIndex(), (11) * 256);

        cell.setCellValue(StringUtils.stripToEmpty(text));
        return this;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public XlsxBuilder addMergedColumn(int numRow, int untilRow, int numCol, int untilCol) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        sheet.addMergedRegion(cellRangeAddress);
        return this;
    }

    public void setBordersToMergedCells() {
        List<CellRangeAddress> mergedRegions = new ArrayList<>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            mergedRegions.add(sheet.getMergedRegion(i));
        }

        for (CellRangeAddress rangeAddress : mergedRegions) {
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, rangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, rangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, rangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, rangeAddress, sheet, workbook);
        }
    }


    public XlsxBuilder addBoldTextLeftAlignedColumn(String text) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_LEFT, StyleAttribute.BOLD);
        cell.setCellStyle(style);
        cell.setCellValue(StringUtils.stripToEmpty(text));
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }


    public XlsxBuilder addBoldTextCenterAlignedColumn(String text) {
        Cell cell = row.createCell(nextColumnIdx);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_CENTER, StyleAttribute.BOLD);

        cell.setCellStyle(style);
        cell.setCellValue(StringUtils.stripToEmpty(text));
        nextColumnIdx = nextColumnIdx + 1;
        return this;
    }

    public XlsxBuilder addSameBoldTextCenterAlignedColumn(String text) {
        sheet.getRow(1);
        Cell cell = sheet.getRow(0).getCell(2);
        CellStyle style = getCellStyle(StyleAttribute.ALIGN_CENTER, StyleAttribute.BOLD);
        cell.setCellStyle(style);
        cell.setCellValue(StringUtils.stripToEmpty(text));
        return this;
    }


    /**
     * Builds the result object.
     * Object cannot be reused after calling this method.
     *
     * @return created object
     */
    public byte[] build() {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
        return bos.toByteArray();
    }

    /**
     * Returns cell style.
     *
     * @param attrs attributes
     * @return cell style
     */
    private CellStyle getCellStyle(StyleAttribute... attrs) {
        Set<StyleAttribute> allattrs = new HashSet<StyleAttribute>();
        allattrs.addAll(rowStyleAttributes);
        allattrs.addAll(Arrays.asList(attrs));
        if (styleBank.containsKey(allattrs)) {
            return styleBank.get(allattrs);
        }
        CellStyle style = workbook.createCellStyle();
        Font txtFont = workbook.createFont();
        txtFont.setFontName("Times New Roman");
        txtFont.setFontHeightInPoints((short)7);
        style.setFont(txtFont);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        for (StyleAttribute attr : allattrs) {
            if (attr.equals(StyleAttribute.TITLE_SIZE)) {
                txtFont.setFontHeightInPoints((short) 18);
            } else if (attr.equals(StyleAttribute.BOLD)) {
                txtFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            } else if (attr.equals(StyleAttribute.THIN_TOP_BORDER)) {
                style.setBorderTop(CellStyle.BORDER_THIN);
            } else if (attr.equals(StyleAttribute.THIN_BOTTOM_BORDER)) {
                style.setBorderBottom(CellStyle.BORDER_THIN);
            } else if (attr.equals(StyleAttribute.THICK_TOP_BORDER)) {
                style.setBorderTop(CellStyle.BORDER_THICK);
            } else if (attr.equals(StyleAttribute.THICK_BOTTOM_BORDER)) {
                style.setBorderBottom(CellStyle.BORDER_THICK);
            } else if (attr.equals(StyleAttribute.ALIGN_LEFT)) {
                style.setAlignment(CellStyle.ALIGN_LEFT);
            } else if (attr.equals(StyleAttribute.ALIGN_CENTER)) {

            } else {
                throw new RuntimeException("unknown cell style attribute: " + attr);
            }
        }
        styleBank.put(allattrs, style);
        return style;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Possible style attributes.
     */
    private static enum StyleAttribute {
        /**
         * Thin top border.
         */
        THIN_TOP_BORDER,
        /**
         * Thin bottom border.
         */
        THIN_BOTTOM_BORDER,
        /**
         * Thick top border.
         */
        THICK_TOP_BORDER,
        /**
         * Thick bottom border.
         */
        THICK_BOTTOM_BORDER,
        /**
         * Title font size.
         */
        TITLE_SIZE,
        /**
         * Bold font.
         */
        BOLD,
        /**
         * Left alignment.
         */
        ALIGN_LEFT,
        /**
         * Center alignment.
         */
        ALIGN_CENTER
    }

    ;
}
