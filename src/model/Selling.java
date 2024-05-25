//Selling.java
package model;


import java.math.BigDecimal;
import java.util.Date;

public class Selling {
    private int id_selling;
    private int id;
    private int id_player;
    private Date selling_date;
    private BigDecimal total_payment;

    // Getter and setter for each attribute
    public int getIdSelling() {
        return id_selling;
    }

    public void setIdSelling(int id_selling) {
        this.id_selling = id_selling;
    }

    public int getUserId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id_player;
    }

    public void setIdPlayer(int id_player) {
        this.id_player = id_player;
    }

    public Date getSellingDate() {
        return selling_date;
    }

    public void setSellingDate(Date selling_date) {
        this.selling_date = selling_date;
    }

    public BigDecimal getTotalPayment() {
        return total_payment;
    }

    public void setTotalPayment(BigDecimal total_payment) {
        this.total_payment = total_payment;
    }
}
