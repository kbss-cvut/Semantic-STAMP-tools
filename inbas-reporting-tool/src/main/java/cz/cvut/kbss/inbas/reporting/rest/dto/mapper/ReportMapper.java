package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import org.mapstruct.Mapper;

import java.util.SplittableRandom;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class ReportMapper {

    private final SplittableRandom random = new SplittableRandom();

}
