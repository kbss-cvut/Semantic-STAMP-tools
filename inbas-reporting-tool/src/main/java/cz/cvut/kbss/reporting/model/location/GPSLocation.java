package cz.cvut.kbss.reporting.model.location;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.Objects;

@OWLClass(iri = Vocabulary.s_c_gps_location)
public class GPSLocation extends Location {

    @OWLDataProperty(iri = Vocabulary.s_p_lat)
    @ParticipationConstraints(nonEmpty = true)
    private Double latitude;

    @OWLDataProperty(iri = Vocabulary.s_p__long)
    @ParticipationConstraints(nonEmpty = true)
    private Double longitude;

    public GPSLocation() {
    }

    public GPSLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Location copy() {
        return new GPSLocation(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GPSLocation that = (GPSLocation) o;
        return Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "GPSLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
