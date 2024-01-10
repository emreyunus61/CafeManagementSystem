package com.example.cafemanagementsystem.service.Impl;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.entity.Bill;
import com.example.cafemanagementsystem.jwt.JwtFilter;
import com.example.cafemanagementsystem.repository.BillRepository;
import com.example.cafemanagementsystem.service.BillService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillRepository billRepository;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requsetMap) {
        log.info("Inseide generateReport");
        try {
            String fileName;
            if (validateRequestMap(requsetMap)) {
                if (requsetMap.containsKey("isGenerate") && !(Boolean)requsetMap.get("isGenerate")){
                    fileName=(String) requsetMap.get("uuid");
                }else {
                    fileName=CafeUtils.getUUID();
                    requsetMap.put("uuid",fileName);
                    insertBill(requsetMap);
                }

                String data = "Name: "+ requsetMap.get("name") + "\n" + "Contact Number: "+requsetMap.get("contactNumber")+
                        "\n" + "Email: " + requsetMap.get("email") + "\n" + "Payment Method: " +requsetMap.get("paymentMethod");

                Document document= new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstans.STORE_LOCATION+"\\"+fileName+".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk=new Paragraph("Cafe Management System",getFont1("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph= new Paragraph(data+"\n \n",getFont1("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requsetMap.get("productDetails"));
                for (int i=0; i<jsonArray.length();i++){
                    addRows(table,CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total : "+requsetMap.get("totalAmount") + "\n" +
                        "Thank you for visiting.Please visit again!!", getFont1("Data"));

                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity("Required data not found.", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name","Category","Quantity","Price","Sub Total")
                .forEach(columTitle->{
                    PdfPCell header= new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont1(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return  headerFont;
            case  "Data" :
                Font dataFont= FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return  dataFont;
            default:
                return  new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> requsetMap) {
        Bill bill = new Bill();
        bill.setUuid((String) requsetMap.get("uuid"));
        bill.setName((String) requsetMap.get("name"));
        bill.setEmail((String) requsetMap.get("email"));
        bill.setContactNumber((String) requsetMap.get("contactNumber"));
        bill.setPaymentMethod((String) requsetMap.get("paymentMethod"));
        bill.setTotal(Integer.parseInt((String) requsetMap.get("totalAmount")));
        bill.setProductDetail((String) requsetMap.get("productDetails"));
        bill.setCreatedBy(jwtFilter.getUserName());
        billRepository.save(bill);
    }

    private boolean validateRequestMap(Map<String, Object> requsetMap) {
      return  requsetMap.containsKey("name") &&
              requsetMap.containsKey("contactNumber") &&
              requsetMap.containsKey("email") &&
              requsetMap.containsKey("paymentMethod") &&
              requsetMap.containsKey("productDetails") &&
              requsetMap.containsKey("totalAmount");
    }


    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill>  list = new ArrayList<>();
        if (jwtFilter.isAdmin()){
            list=billRepository.getAllBills();
        }else {
            list= billRepository.getBillByUserName(jwtFilter.getUserName());
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requsetMap) {
        log.info("Inside getPdf : requestMap {}",requsetMap);
        try {
            byte[] byteArray = new byte[0];
            if (!requsetMap.containsKey("uuid") && validateRequestMap(requsetMap))
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            String filePath=CafeConstans.STORE_LOCATION+"\n"+(String) requsetMap.get("uuid")+ ".pdf";
            if (CafeUtils.isFileExist(filePath)){
                byteArray = getByteArray(filePath);
                return  new ResponseEntity<>(byteArray,HttpStatus.OK);
            }else {
                requsetMap.put("isGenerate",false);
                generateReport(requsetMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    private byte[] getByteArray(String filePath) throws Exception {

        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[]  byteArray= IOUtils.toByteArray(targetStream);
        return  byteArray;
    }



    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
       try {
           Optional optional = billRepository.findById(id);
           if (!optional.isEmpty()){
               billRepository.deleteById(id);
               return CafeUtils.getResponseEntity("Bill Deleted Sucesfully",HttpStatus.OK);
           }
           return CafeUtils.getResponseEntity("Bill id does not exist",HttpStatus.OK);

       }catch (Exception ex){
           ex.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
