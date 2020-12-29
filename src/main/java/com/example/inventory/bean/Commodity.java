package com.example.inventory.bean;

import javax.persistence.*;

/**
 * description: Commodity
 * date: 12/29/20 4:21 AM
 * author: fourwood
 */
@Entity
@Table(name = "commodity")
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commodty_id")
    private Long commodityId;

    @Column(name = "commodity_name")
    private String commodityName;

    @Column(name = "commodity_price")
    private Integer commodityPrice;

    @Column(name = "commodity_color")
    private String commodityColor;

    @Column(name = "commodity_image")
    private String commodityImage;

    @Column(name = "commodity_specification")
    private String commoditySpecification;

    @Column(name = "commodity_inventory")
    private Integer commodityInventory;

    @Column(name = "commodity_type")
    private CommodityType commodityType;

    @Column(name = "introduction")
    private String introduction;

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityId=" + commodityId +
                ", commodityName='" + commodityName + '\'' +
                ", commodityPrice=" + commodityPrice +
                ", commodityColor='" + commodityColor + '\'' +
                ", commodityImage='" + commodityImage + '\'' +
                ", commoditySpecification='" + commoditySpecification + '\'' +
                ", commodityInventory=" + commodityInventory +
                ", commodityType=" + commodityType +
                ", introduction='" + introduction + '\'' +
                '}';
    }

    public String getIntroduction() {
        return introduction;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public Integer getCommodityPrice() {
        return commodityPrice;
    }

    public String getCommodityColor() {
        return commodityColor;
    }

    public String getCommodityImage() {
        return commodityImage;
    }

    public String getCommoditySpecification() {
        return commoditySpecification;
    }

    public Integer getCommodityInventory() {
        return commodityInventory;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public void setCommodityPrice(Integer commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public void setCommodityColor(String commodityColor) {
        this.commodityColor = commodityColor;
    }

    public void setCommodityImage(String commodityImage) {
        this.commodityImage = commodityImage;
    }

    public void setCommoditySpecification(String commoditySpecification) {
        this.commoditySpecification = commoditySpecification;
    }

    public void setCommodityInventory(Integer commodityInventory) {
        this.commodityInventory = commodityInventory;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Commodity(String commodityName, Integer commodityPrice, String commodityColor, String commodityImage, String commoditySpecification, Integer commodityInventory, CommodityType commodityType, String introduction){
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityColor = commodityColor;
        this.commodityImage = commodityImage;
        this.commoditySpecification = commoditySpecification;
        this.commodityInventory = commodityInventory;
        this.commodityType = commodityType;
        this.introduction = introduction;
    }

    public Commodity() {}

    public void setCommodity(String commodityName, Integer commodityPrice, String commodityColor, String commodityImage, String commoditySpecification, Integer commodityInventory, CommodityType commodityType, String introduction){
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityColor = commodityColor;
        this.commodityImage = commodityImage;
        this.commoditySpecification = commoditySpecification;
        this.commodityInventory = commodityInventory;
        this.commodityType = commodityType;
        this.introduction = introduction;
    }
}
