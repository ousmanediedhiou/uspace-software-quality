package uspace.domain.account;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "username", nullable = false, updatable = false, length = 64)
    private String username;

    @Column(name = "home_planet_name", nullable = false, length = 64)
    private String homePlanetName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_booking_ids", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "booking_id", nullable = false, length = 36)
    private List<String> bookingIds = new ArrayList<>();

    protected Account() { }

    public Account(String username, String homePlanetName) {
        this.username = Objects.requireNonNull(username);
        this.homePlanetName = Objects.requireNonNull(homePlanetName);
    }

    public String getUsername() { return username; }
    public String getHomePlanetName() { return homePlanetName; }
    public List<String> getBookingIds() { return bookingIds; }

    public void addBookingId(String bookingId) {
        if (!bookingIds.contains(bookingId)) {
            bookingIds.add(bookingId);
        }
    }
}
