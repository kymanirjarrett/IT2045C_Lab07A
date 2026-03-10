/**
 * Represents customer information for an invoice.
 */
public class Customer {
    private String name;
    private String street;
    private String city;
    private String state;
    private String zip;

    public Customer(String name, String street, String city, String state, String zip) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getFormattedAddress() {
        return name + "\n"
                + street + "\n"
                + city + ", " + state + " " + zip;
    }
}