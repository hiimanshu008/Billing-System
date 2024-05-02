package com.inn.bill.rest;

import com.inn.bill.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String , Object> requedtMap);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String , Object> requedtMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String>deleteBill(@PathVariable Integer id);

}
