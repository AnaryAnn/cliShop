package application.data;

import javax.persistence.*;

@Table(name = "item")
@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private Double sum;

    @Column
    private String currency;

    public Item(String name, String category, Double sum, String currency) {
        this.name = name;
        this.category = category;
        this.sum = sum;
        this.currency = currency;
    }

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
