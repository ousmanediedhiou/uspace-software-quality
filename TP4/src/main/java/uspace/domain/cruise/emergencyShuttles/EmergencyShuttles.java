package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.*;
import uspace.domain.cruise.money.Money;

import java.util.ArrayList;
import java.util.List;

@Entity
public class EmergencyShuttles {

    @Id
    private EmergencyShuttlesId id;

    private Money cost;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Shuttle> shuttles;

    public EmergencyShuttles(){
        this.id = new EmergencyShuttlesId();
        this.cost = new Money(0);
        this.shuttles = new ArrayList<>();
    }

    public EmergencyShuttles(EmergencyShuttlesId id, Money cost, List<Shuttle> shuttles) {
        this.id = id;
        this.cost = cost;
        this.shuttles = shuttles;
    }

    public Money getCost() {
        return cost;
    }

    public List<Shuttle> getShuttles() {
        return shuttles;
    }

    public Shuttle findFirstNonFullShuttle(ShuttleType type){
        for (Shuttle shuttle : shuttles) {
            if( (shuttle.getType() == type) && (!shuttle.isFull()) ){
                return shuttle;
            }
        }
        return null;
    }

    public Integer numberOfShuttlesOfType(ShuttleType type){
        return shuttles.stream().filter(s -> s.getType() == type).toList().size();
    }

    public void addShuttle(Shuttle shuttle){
        shuttles.add(shuttle);
        cost = cost.add(shuttle.getCost());
    }
}
