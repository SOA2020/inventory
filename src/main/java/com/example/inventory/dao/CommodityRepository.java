package com.example.inventory.dao;

import com.example.inventory.bean.Commodity;
import com.example.inventory.bean.CommodityType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * description: CommodityRepository
 * date: 12/29/20 4:52 AM
 * author: fourwood
 */
public interface CommodityRepository extends CrudRepository<Commodity, Long> {
    Commodity findById(long commodityId);

//    @Transactional
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    Commodity findCommodityByCommodityId(long commodityId);

    ArrayList<Commodity> findByCommodityType(CommodityType commodityType);
}
