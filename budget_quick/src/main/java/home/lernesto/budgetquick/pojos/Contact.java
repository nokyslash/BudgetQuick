package home.lernesto.budgetquick.pojos;


public class Contact {
    private final int IdP;
    private final String phone;
    private final String name;

    public Contact(int idP, String phone, String name) {
        IdP = idP;
        this.phone = phone;
        this.name = name;
    }

    public int getIdP() {
        return IdP;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}
