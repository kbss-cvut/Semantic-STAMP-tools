package cz.cvut.kbss.inbas.audit.dto;

public class Link {

    private FactorDto from;
    private FactorDto to;

    public Link() {
    }

    public Link(FactorDto from, FactorDto to) {
        this.from = from;
        this.to = to;
    }

    public FactorDto getFrom() {
        return from;
    }

    public void setFrom(FactorDto from) {
        this.from = from;
    }

    public FactorDto getTo() {
        return to;
    }

    public void setTo(FactorDto to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Link{" + from + " -> " + to + '}';
    }
}
