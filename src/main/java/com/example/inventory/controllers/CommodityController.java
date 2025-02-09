package com.example.inventory.controllers;

import com.example.inventory.service.CommodityService;
import com.example.inventory.bean.Commodity;
import com.example.inventory.bean.CommodityType;
import com.example.inventory.dao.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private CommodityService commodityService;
    public static final int PG_SIZE = 10;

    @RequestMapping(value = "/commodity", method = RequestMethod.GET)
    public HashMap<String, Object> getCommodity(@RequestParam(defaultValue = "-1") int type , @RequestParam(defaultValue = "1") int pageNumber){
        List<Commodity> commodityList;
        if(type >= 0)
            commodityList = commodityRepository.findByCommodityType(CommodityType.values()[type]);
        else
            commodityList = (List<Commodity>) commodityRepository.findAll();

        int sz = commodityList.size();
        int pgStart = (pageNumber - 1) * PG_SIZE;
        int pgEnd = pageNumber * PG_SIZE;
        pgEnd = Math.min(pgEnd, sz);
        try {
            commodityList = commodityList.subList(pgStart, pgEnd);
        }catch (IndexOutOfBoundsException | IllegalArgumentException e){
            commodityList.clear();
        }

        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("commodities", commodityList);
        response.put("count", sz);
        response.put("pgNum", pageNumber);
        response.put("pgSize", PG_SIZE);
        return response;
    }

    @RequestMapping(value = "/commodity", method = RequestMethod.POST)
    public ResponseEntity createCommodity(@RequestBody HashMap<String, Object> postInfo){
        String name = postInfo.getOrDefault("commodityName", null).toString();
        int price = (int)postInfo.getOrDefault("commodityPrice", -1);
        String color = postInfo.getOrDefault("commodityColor", null).toString();
        String spec = postInfo.getOrDefault("commoditySpecification", null).toString();
        int inventory = (int)postInfo.getOrDefault("commodityInventory", -1);
        int type = (int)postInfo.getOrDefault("commodityType", -1);
        String intro = postInfo.getOrDefault("introduction", null).toString();
        if(name == null || color == null || spec == null || intro == null
                || price < 0 || inventory < 0 || type < 0){
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        String image = "commodity";
        Commodity commodity = new Commodity(name, price, color, image, spec, inventory, CommodityType.values()[type], intro);
        commodity =  commodityRepository.save(commodity);
        Long commodityId = commodity.getCommodityId();
        commodity.setCommodityImage(image + commodityId.toString());
        commodity =  commodityRepository.save(commodity);
        return new ResponseEntity<>(commodity, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/commodity/{commodityId}", method = RequestMethod.GET)
    public ResponseEntity getCommodityById(@PathVariable long commodityId){
        Commodity commodity = commodityRepository.findById(commodityId);
        if(commodity == null){
            return new ResponseEntity<>("Commodity Not Found!", HttpStatus.NOT_FOUND);
        }else{
            HashMap<String, Object> response = new HashMap<String, Object>();
            response.put("commodityId", commodity.getCommodityId());
            response.put("commodityName", commodity.getCommodityName());
            response.put("commodityPrice", commodity.getCommodityPrice());
            response.put("commodityColor", commodity.getCommodityColor());
            response.put("commodityImage", commodity.getCommodityImage());
            response.put("commoditySpecification", commodity.getCommoditySpecification());
            response.put("commodityInventory", commodity.getCommodityInventory());
            response.put("commodityType", commodity.getCommodityType().ordinal());
            response.put("introduction", commodity.getIntroduction());
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @RequestMapping(value = "/commodity/{commodityId}/number", method = RequestMethod.PUT)
    public ResponseEntity addNumber(@RequestParam Integer num, @PathVariable long commodityId) throws InterruptedException {
        return commodityService.addNumber(commodityId, num);
    }

    @RequestMapping(value = "/commodity/search", method = RequestMethod.POST)
    public ResponseEntity search(@RequestBody HashMap<String, ArrayList<String>> body){
        ArrayList<String> keyWords = body.getOrDefault("keyWords", null);
        if(keyWords == null) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        HashMap<Long, Commodity> commodities = new HashMap<>();
        for(String keyWord : keyWords){
            ArrayList<Commodity> commodityArrayList = commodityRepository.findByIntroductionContaining(keyWord);
            for(Commodity commodity : commodityArrayList){
                commodities.put(commodity.getCommodityId(), commodity);
            }
        }

        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("commodities", commodities.values());
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
