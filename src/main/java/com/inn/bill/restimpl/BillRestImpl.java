package com.inn.bill.restimpl;

import com.inn.bill.POJO.Bill;
import com.inn.bill.constants.RetailConstants;
import com.inn.bill.rest.BillRest;
import com.inn.bill.service.BillService;
import com.inn.bill.utils.RetailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requedtMap) {
        try{
            return billService.generateReport(requedtMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            return billService.getBills();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requedtMap) {
        try{
            return billService.getPdf(requedtMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            return billService.deleteBill(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
