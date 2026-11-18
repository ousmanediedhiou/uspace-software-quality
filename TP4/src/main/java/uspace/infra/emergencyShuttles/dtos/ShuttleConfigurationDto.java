package uspace.infra.emergencyShuttles.dtos;

public class ShuttleConfigurationDto {
    public String type;
    public Integer capacity;
    public Integer amount;
    public Integer cost;
    public Boolean unlimited;

    public ShuttleConfigurationDto() {}

    public ShuttleConfigurationDto(String type, Integer capacity, Integer amount, Integer cost, Boolean unlimited) {
        this.type = type;
        this.capacity = capacity;
        this.amount = amount;
        this.cost = cost;
        this.unlimited = unlimited;
    }
}
