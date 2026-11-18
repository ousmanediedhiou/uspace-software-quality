package uspace.application.account.dtos;

import java.util.List;

public class AccountDto {
    public String username;
    public String homePlanet;
    public List<String> bookingIds;

    public AccountDto(String username, String homePlanet, List<String> bookingIds) {
        this.username = username;
        this.homePlanet = homePlanet;
        this.bookingIds = bookingIds;
    }
}
