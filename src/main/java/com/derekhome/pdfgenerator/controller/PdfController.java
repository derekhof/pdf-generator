package com.derekhome.pdfgenerator.controller;

import com.derekhome.pdfgenerator.model.Bill;
import com.derekhome.pdfgenerator.model.Invoice;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;



@RestController
public class PdfController {

    @RequestMapping(value = "/pdf-getInvoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> Invoice(@RequestBody Invoice invoice) throws IOException {

        // Create pdf
        ByteArrayInputStream bis = createPdf(invoice);
        HttpHeaders headers = new HttpHeaders();

        // Return pdf
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    public static ByteArrayInputStream createPdf(Invoice invoice) throws IOException {

        // Write the pdf output to the Byte array output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);

        // Create a document
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Paragraph company information
        Paragraph company_info = new Paragraph();
        company_info.add("Company: ");
        company_info.add(invoice.getCompany_name());
        company_info.add("\nAddress: ").setTextAlignment(TextAlignment.LEFT);
        company_info.add(invoice.getAddress() + " " + invoice.getHousenumber());
        company_info.add("\nZipcode: ");
        company_info.add(invoice.getZip_code() + " " + invoice.getCity());
        company_info.add("\nInvoice date: ");
        company_info.add(invoice.getInvoice_date());

        Paragraph billing_period = new Paragraph();
        billing_period.add("Period: " + invoice.getBill_period());


        // Create layout of pdf
        document.setTextAlignment(TextAlignment.CENTER).setFontSize(20);
        document.add(new Paragraph("VMX Virtual Machine Excellence"));
        document.setTextAlignment(TextAlignment.LEFT).setFontSize(18);
        document.add(new Paragraph("Invoice"));
        document.setTextAlignment(TextAlignment.LEFT).setFontSize(8);
        document.add(company_info);
        document.add(billing_period);
        document.add(createTable(invoice));


        Paragraph payment = new Paragraph();
        payment.add("Payment will be automatically collected from your creditcard account").setFontSize(5).setBold();
        document.add(payment);


        // Close document to write it to the output stream
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }


    public static Table createTable(Invoice invoice) throws IOException {

        // Table headers
        String[] headers = {"Name virtual machine", "Operating system", "Service level", "Up time days", "Costs"};

        // Create table
        float[] columnWidths = {100, 100, 100, 100, 100};
        Table table = new Table(columnWidths);
        table.setWidth(500);
        PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);


        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header))
                    .setBold()
                    .setFontSize(7)
                    .setFontColor(DeviceGray.WHITE)
                    .setBackgroundColor(DeviceGray.GRAY)
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(cell);
        }

        // variabele used for counting the total costs
        Float total_amount = 0.0f;

        for (Bill bill : invoice.getBills()) {

            // Fill table with data from bills
            table.addCell(bill.getVm_name()).setFontSize(7).setTextAlignment(TextAlignment.CENTER);
            table.addCell(bill.getOperating_system()).setFontSize(7).setTextAlignment(TextAlignment.CENTER);
            table.addCell(bill.getService_level()).setFontSize(7).setTextAlignment(TextAlignment.CENTER);
            table.addCell(String.valueOf(Math.round(bill.getMonth_days()))).setFontSize(7).setTextAlignment(TextAlignment.CENTER);
            table.addCell(String.valueOf(Math.round(bill.getMonth_costs_vm() * 100.0) / 100.0)).setFontSize(7).setTextAlignment(TextAlignment.CENTER);

            // count total amount of the costs
            total_amount = total_amount + bill.getMonth_costs_vm();
        }


        // Create the last row with total costs
        Cell total_colspan = new Cell(1, 3)
                .setFont(f)
                .setFontSize(7)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.GRAY);
        table.addCell(total_colspan);

        Cell total_name = new Cell()
                .add(new Paragraph("Total costs: "))
                .setFont(f)
                .setFontSize(7)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(total_name);

        Cell total_costs = new Cell()
                .add(new Paragraph(String.valueOf(Math.round(total_amount * 100.0) / 100.0)))
                .setFontSize(7)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(total_costs);

        return table;
    }

}
