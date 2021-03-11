package simpleapps.kafkapoc;

public class CustomerEmployeeComposition {

    private final Customer customer;
    private final Employee employee;

    public CustomerEmployeeComposition(final Customer customer, final Employee employee) {
        this.customer = customer;
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "CustomerEmployeeComposition{" +
                "customer=" + customer +
                ", employee=" + employee +
                '}';
    }
}
