package es.udc.fic.csi2122.baserest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The Sale entity
 *
 * @author hugo.martinez.vazquez@udc.es
 */
@Entity
public class Sale {

    @Id
    @GeneratedValue
    private Long id;

    //Id del producto
    private Long prodId;

    //Id del producto
    private Long clientId;

    //Unidades del producto
    @Column
    private Integer unit;

    //Coste del producto
    @Column
    private float cost;

    //Impuestos del producto
    @Column
    private float tax;

    public Sale() {
    }

    /**
     * Convenience constructor for new sales. Id is not set
     *
     * @param prodId    the product id
     * @param clientId  the client id
     * @param unit      the number of units sold
     * @param cost      the cost of all the units
     * @param tax       the tax imposed over the sale
     */
    public Sale(Long prodId, Long clientId, Integer unit, Float cost, Float tax) {
        this.prodId = prodId;
        this.clientId = clientId;
        this.unit = unit;
        this.cost = cost;
        this.tax = tax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return prodId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax (Float tax) {
        this.tax = tax;
    }
}