package uspace.application.cruise.cabin.dtos;

import java.util.List;

public class CabinAttributionsDto {
    public List<CabinAttributionDto> cabins;

    public CabinAttributionsDto() {
    }

    public CabinAttributionsDto(List<CabinAttributionDto> cabins) {
        this.cabins = cabins;
    }
}
