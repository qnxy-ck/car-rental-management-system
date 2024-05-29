package com.qnxy.common.data.ui;

import java.util.Objects;

/**
 * @author Qnxy
 */
public class AdminTableData {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 车型号
     */
    private String carModel;

    /**
     * 所有者
     */
    private String carOwner;

    /**
     * 价格(元/天)
     */
    private String price;

    /**
     * 车颜色
     */
    private String carColor;

    /**
     * 是否被租用
     */
    private Boolean leased;

    /**
     * 租用者
     */
    private String leasedUser;

    public AdminTableData() {
    }

    public AdminTableData(Integer id, String carModel, String carOwner, String price, String carColor, Boolean leased, String leasedUser) {
        this.id = id;
        this.carModel = carModel;
        this.carOwner = carOwner;
        this.price = price;
        this.carColor = carColor;
        this.leased = leased;
        this.leasedUser = leasedUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public Boolean getLeased() {
        return leased;
    }

    public void setLeased(Boolean leased) {
        this.leased = leased;
    }

    public String getLeasedUser() {
        return leasedUser;
    }

    public void setLeasedUser(String leasedUser) {
        this.leasedUser = leasedUser;
    }

    @Override
    public String toString() {
        return "AdminTableData{" +
                "id=" + id +
                ", carModel='" + carModel + '\'' +
                ", carOwner='" + carOwner + '\'' +
                ", price=" + price +
                ", carColor='" + carColor + '\'' +
                ", leased=" + leased +
                ", leasedUser='" + leasedUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminTableData that = (AdminTableData) o;
        return Objects.equals(id, that.id) && Objects.equals(carModel, that.carModel) && Objects.equals(carOwner, that.carOwner) && Objects.equals(price, that.price) && Objects.equals(carColor, that.carColor) && Objects.equals(leased, that.leased) && Objects.equals(leasedUser, that.leasedUser);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(carModel);
        result = 31 * result + Objects.hashCode(carOwner);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(carColor);
        result = 31 * result + Objects.hashCode(leased);
        result = 31 * result + Objects.hashCode(leasedUser);
        return result;
    }
}
