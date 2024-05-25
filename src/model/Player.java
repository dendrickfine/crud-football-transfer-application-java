//Player.java
package model;

import java.math.BigDecimal;

public class Player {
    private int id_player; // Menambahkan atribut id_player
    private String name_player;
    private Position position;
    private String citizenship;
    private int age;
    private Foot foot;
    private BigDecimal market_value;
    private BigDecimal sell_price;

    // Getter and setter for each attribute
    public int getId() {
        return id_player;
    }

    public void setId(int id_player) {
        this.id_player = id_player;
    }

    public String getName() {
        return name_player;
    }

    public void setName(String name_player) {
        this.name_player = name_player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Foot getFoot() {
        return foot;
    }

    public void setFoot(Foot foot) {
        this.foot = foot;
    }

    public BigDecimal getMarketValue() {
        return market_value;
    }

    public void setMarketValue(BigDecimal market_value) {
        this.market_value = market_value;
    }

    public BigDecimal getSellPrice() {
        return sell_price;
    }

    public void setSellPrice(BigDecimal sell_price) {
        this.sell_price = sell_price;
    }

    // Enum for position
    public enum Position {
        goalkeeper,
        defender,
        midfielder,
        striker
    }

    // Enum for foot
    public enum Foot {
        right,
        left,
        both
    }
}
