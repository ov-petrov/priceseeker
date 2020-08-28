package price;

public class Price implements Comparable<Price> {
    private Integer id;
    private String name;
    private String condition;
    private String state;
    private Float price;

    public Price() {
    }

    public Price(Integer id, String name, String condition, String state, Float price) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.state = state;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public int compareTo(Price o) {
        return this.price.compareTo(o.getPrice());
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", price=" + price;
    }
}
