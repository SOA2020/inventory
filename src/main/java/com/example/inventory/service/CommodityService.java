package com.example.inventory.service;

import com.example.inventory.bean.Commodity;
import com.example.inventory.dao.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

/**
 * description: CommodityService
 * date: 1/1/21 6:21 PM
 * author: fourwood
 */
@Service
public class CommodityService {

    @Autowired
    private CommodityRepository commodityRepository;

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public ResponseEntity addNumber(long commodityId, int num) throws InterruptedException {
        Thread.sleep(2000);
        Commodity commodity = commodityRepository.findById(commodityId);
        Integer inventory = commodity.getCommodityInventory();
        if(num + inventory < 0){
            return new ResponseEntity<>("Out Of Stock", HttpStatus.BAD_REQUEST);
        }
        commodity.setCommodityInventory(inventory + num);
        commodity = commodityRepository.save(commodity);
        return new ResponseEntity(commodity, HttpStatus.OK);
    }

}
