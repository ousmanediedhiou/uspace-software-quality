package uspace.application.cruise.cabin;

import uspace.application.cruise.cabin.dtos.CabinAttributionDto;
import uspace.application.cruise.cabin.dtos.CabinAttributionsDto;

import java.util.List;

public class CabinAttributionAssembler {

    public CabinAttributionsDto toDto(List<CabinAttributionResult> results) {
        List<CabinAttributionDto> dtos = results.stream()
                .map(result -> new CabinAttributionDto(
                        result.getCabinId(),
                        result.getBookingId(),
                        result.getCabinType().toString()))
                .toList();

        return new CabinAttributionsDto(dtos);
    }
}
