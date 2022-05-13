package es.udc.fic.csi2122.baserest.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//Entidad de producto
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    //Nombre del producto
    private String name;

    //Precio del producto
    @Column
    private float price;

    //Stock del producto
    @Column
    private Integer stock;

    //Breve descripcion del producto
    @Column
    private String description;

    public void setId(Long id) {
        this.id = id;
    }

    public Product() {

    }

    public Product( String name, float price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Creamos un metodo toString que nos puede ser util en un futuro
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                '}';
    }
}
