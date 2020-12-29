package com.example.inventory.controllers;

import com.example.inventory.bean.Commodity;
import com.example.inventory.bean.CommodityType;
import com.example.inventory.dao.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;

/**
 * description: CommodityController
 * date: 12/29/20 4:54 AM
 * author: fourwood
 */
@RestController
public class CommodityController {
    @Autowired
    private CommodityRepository commodityRepository;
    public static final int PG_SIZE = 10;

    @RequestMapping(value = "/commodity", method = RequestMethod.GET)
    public HashMap<String, Object> getCommodity(@RequestParam(defaultValue = "-1") int type , @RequestParam(defaultValue = "1") int pgNum){
        List<Commodity> commodityList;
        if(type >= 0)
            commodityList = commodityRepository.findByCommodityType(CommodityType.values()[type]);
        else
            commodityList = (List<Commodity>) commodityRepository.findAll();

        int sz = commodityList.size();
        int pgStart = (pgNum - 1) * PG_SIZE;
        int pgEnd = pgNum * PG_SIZE;
        pgEnd = Math.min(pgEnd, sz);
        try {
            commodityList = commodityList.subList(pgStart, pgEnd);
        }catch (IndexOutOfBoundsException | IllegalArgumentException e){
            commodityList.clear();
        }

        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("commodities", commodityList);
        response.put("count", sz);
        response.put("pgNum", pgNum);
        response.put("pgSize", PG_SIZE);
        return response;
    }

    @RequestMapping(value = "/commodity", method = RequestMethod.POST)
    public ResponseEntity createCommodity(@RequestBody HashMap<String, Object> postInfo){
        String name = postInfo.getOrDefault("commodityName", null).toString();
        int price = (int)postInfo.getOrDefault("commodityPrice", -1);
        String color = postInfo.getOrDefault("commodityColor", null).toString();
        String image = postInfo.getOrDefault("commodityImage", null).toString();
        String spec = postInfo.getOrDefault("commoditySpecification", null).toString();
        int inventory = (int)postInfo.getOrDefault("commodityInventory", -1);
        int type = (int)postInfo.getOrDefault("commodityType", -1);
        String intro = postInfo.getOrDefault("introduction", null).toString();
        if(name == null || color == null || image == null || spec == null || intro == null
                || price < 0 || inventory < 0 || type < 0){
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        Commodity commodity = new Commodity(name, price, color, image, spec, inventory, CommodityType.values()[type], intro);
        commodity =  commodityRepository.save(commodity);
        return new ResponseEntity<>(commodity, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/commodity/{commodityId}", method = RequestMethod.GET)
    public ResponseEntity getCommodityById(@PathVariable long commodityId){
        Commodity commodity = commodityRepository.findById(commodityId);
        if(commodity == null){
            return new ResponseEntity<>("Commodity Not Found!", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(commodity, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/commodity/{commodityId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCommoidty(@PathVariable long commodityId){
        try {
            commodityRepository.deleteById(commodityId);
        }catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Commodity Not Found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/commodity/{commodityId}", method = RequestMethod.PUT)
    public ResponseEntity putCommodity(@PathVariable long commodityId, @RequestBody HashMap<String, Object> postInfo){
        String name = postInfo.getOrDefault("commodityName", null).toString();
        int price = (int)postInfo.getOrDefault("commodityPrice", -1);
        String color = postInfo.getOrDefault("commodityColor", null).toString();
        String image = postInfo.getOrDefault("commodityImage", null).toString();
        String spec = postInfo.getOrDefault("commoditySpecification", null).toString();
        int inventory = (int)postInfo.getOrDefault("commodityInventory", -1);
        int type = (int)postInfo.getOrDefault("commodityType", -1);
        String intro = postInfo.getOrDefault("introduction", null).toString();
        if(name == null || color == null || image == null || spec == null || intro == null
                || price < 0 || inventory < 0 || type < 0){
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        Commodity commodity = commodityRepository.findById(commodityId);
        if(commodity == null){
            return new ResponseEntity<>("Commodity Not Found!", HttpStatus.NOT_FOUND);
        }

        commodity.setCommodity(name, price, color, image, spec, inventory, CommodityType.values()[type], intro);
        commodity = commodityRepository.save(commodity);
        return new ResponseEntity<>(commodity, HttpStatus.OK);
    }
}
