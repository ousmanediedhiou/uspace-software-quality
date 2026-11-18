package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.*;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.traveler.badge.Badge;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "traveler_type")
public abstract class  Traveler {

    @Id
    private TravelerId id;

    @Embedded
    private TravelerName name;

    @Enumerated(EnumType.STRING)
    private TravelerCategory category;

    @ManyToOne(optional = true)
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    @ManyToOne(optional = true)
    @JoinColumn(name = "cruise_id", nullable = true)
    private Cruise cruise;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "traveler_badges", joinColumns = @JoinColumn(name = "traveler_id"))
    @Enumerated(EnumType.STRING)
    private List<Badge> badges = new ArrayList<>();

    protected Traveler() {}

    protected Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges) {
        this.id = id;
        this.name = name;
        this.category = category;
        if (badges != null) this.badges = new ArrayList<>(badges);
    }

    public TravelerId getId() { return id; }
    public TravelerName getName() { return name; }
    public TravelerCategory getCategory() { return category; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Cruise getCruise() { return cruise; }
    public void setCruise(Cruise cruise) { this.cruise = cruise; }

    public List<Badge> getBadges() { return badges; }

    protected void addBadge(Badge badge) {
        if (!badges.contains(badge)) badges.add(badge);
    }

    public abstract void bookZeroGravityExperience(
            uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience z,
            List<Traveler> all);
}
