package phonebook;

public class Contact{
    private String name;
    private String number;

    public Contact(String number, String name) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public boolean isGreaterThan(Contact other) {
        return this.getName().compareToIgnoreCase(other.getName()) > 0;
    }

    public int compareTo(String name) {
        return this.getName().compareToIgnoreCase(name) == 0 ? 0 : this.getName().compareToIgnoreCase(name) > 0 ? 1 : -1;
    }
}
