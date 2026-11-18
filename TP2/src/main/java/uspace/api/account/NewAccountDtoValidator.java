package uspace.api.account;

import uspace.application.account.dtos.NewAccountDto;
import uspace.domain.exceptions.MissingParameterException;

public class NewAccountDtoValidator {
    public void validate(NewAccountDto dto) {
        if (dto == null) {
            throw new MissingParameterException("body");
        }
        if (dto.username == null || dto.username.isBlank()) {
            throw new MissingParameterException("username");
        }
        if (dto.homePlanetName == null || dto.homePlanetName.isBlank()) {
            throw new MissingParameterException("homePlanetName");
        }
    }
}
