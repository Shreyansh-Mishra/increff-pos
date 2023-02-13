package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.IOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;


@Api
@RestController
@RequestMapping(path = "/api")
public class FileController {

    @Autowired
    BrandDto brandDto;
    @Autowired
    ProductDto productDto;
    @Autowired
    InventoryDto inventoryDto;

    @ApiOperation(value = "Uploads a file")
    @RequestMapping(path = "/file", method = RequestMethod.POST)
    public ResponseEntity<Resource> uploadFile(@RequestParam("tsvFile") MultipartFile file, @RequestParam String entity) throws IOException, ApiException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String headerLine = reader.readLine();
        String[] headerRow = headerLine.split("\t");
        HashMap<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.length; i++) { headerMap.put(headerRow[i], i); }
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser parser = CSVParser.parse(reader, CSVFormat.newFormat('\t').withHeader(headerRow).withIgnoreHeaderCase().withTrim());
        List<CSVRecord> records = parser.getRecords();

        List<StatusReport> Exceptions = new ArrayList<>();
        System.out.println(headerMap);
        if(records.size()>5000){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(entity.equals("brand")&&headerMap.containsKey("brand")&&headerMap.containsKey("category")&&headerMap.size()==2){
            Exceptions = brandDto.createBrands(records,headerMap);
        }

        else if(entity.equals("product")&&headerMap.containsKey("name")&&headerMap.containsKey("brandName")&&headerMap.containsKey("category")&&headerMap.containsKey("mrp")&&headerMap.containsKey("barcode")&&headerMap.size()==5){
            Exceptions = productDto.createProducts(records,headerMap);
        }

        else if(entity.equals("inventory")&&headerMap.containsKey("quantity")&&headerMap.containsKey("barcode")&&headerMap.size()==2){
            Exceptions = inventoryDto.addInventories(records,headerMap);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(Exceptions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            ByteArrayResource resource = IOUtil.generateExceptionTsv(Exceptions);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exceptions.tsv");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
    }
}
