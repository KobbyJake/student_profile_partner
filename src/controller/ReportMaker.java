
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package controller;

//~--- non-JDK imports --------------------------------------------------------

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import static controller.IndexController.currentUser;

//~--- JDK imports ------------------------------------------------------------

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Jake
 */
public class ReportMaker extends PdfPageEventHelper {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//    static SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy hh:mm:ss a");

//  static SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMM dd, yyyy hh:mm:ss a");
    static DecimalFormat df = new DecimalFormat("#.###");

    public ReportMaker() {
         try {
                if (currentUser.getAudio().equals("true")) {
                    VoicePrompt.goodjobClip.play(1.0);
                }
            } catch (Exception e) {
            }
    }

    /**
     * The header text.
     */
    String footer;

    /**
     * The template with the total number of pages.
     */
    PdfTemplate total;

    /**
     * Allows us to change the content of the header.
     *
     * @param header The new header String
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    /**
     * Adds a header to every page
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable      table = new PdfPTable(3);
        java.util.Date date  = new java.util.Date();

        try {
            table.setWidths(new int[] { 24, 24, 2 });
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.addCell(footer);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(String.format("©JCS " + sdf.format(date) + ":  " + "Page %d of", writer.getPageNumber()));

            PdfPCell cell = new PdfPCell(com.itextpdf.text.Image.getInstance(total));

            cell.setBorder(Rectangle.BOTTOM);
            table.addCell(cell);
            table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the total number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                                   2, 2, 0);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
