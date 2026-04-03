package home.lernesto.budgetquick.pojos;

public class ClientsPro {
    private final String clientName;
    private final String clientAddress;

//    Constructor
    public ClientsPro(String clientName, String clientAddress) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
    }


//    GET
    public String getName() {
        return clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }
}
