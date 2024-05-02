package com.inn.bill.serviceimpl;

import com.inn.bill.JWT.JwtFilter;
import com.inn.bill.POJO.Category;
import com.inn.bill.POJO.Product;
import com.inn.bill.constants.RetailConstants;
import com.inn.bill.dao.ProductDao;
import com.inn.bill.service.ProductService;
import com.inn.bill.utils.RetailUtils;
import com.inn.bill.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if (validateCategoryMap(requestMap,false)){
                    productDao.save(getProductFromMap(requestMap,false));
                    return RetailUtils.getResponseEntity("Product Added Successfully",HttpStatus.OK);
                }
                else {
                    return RetailUtils.getResponseEntity(RetailConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return RetailUtils.getResponseEntity(RetailConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get( "id")));
        }
        else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }
            else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if (validateCategoryMap(requestMap,false)){
                    Optional<Product> optional =productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        Product product=getProductFromMap(requestMap,true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return RetailUtils.getResponseEntity("Category Updated Successfully",HttpStatus.OK);
                    }
                    else {
                        return RetailUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                    }
                }
                else {
                    return RetailUtils.getResponseEntity(RetailConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return RetailUtils.getResponseEntity(RetailConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                Optional optional=productDao.findById(id);
                if(!optional.isEmpty()){
                    productDao.deleteById(id);
                    return RetailUtils.getResponseEntity("Product deleted successfully",HttpStatus.OK);
                }
                return RetailUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
            }
            else {
                return RetailUtils.getResponseEntity(RetailConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional optional=productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    return RetailUtils.getResponseEntity("Product Status updated successfully",HttpStatus.OK);
                }
                return RetailUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
            }
            else {
                return RetailUtils.getResponseEntity(RetailConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RetailUtils.getResponseEntity(RetailConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
