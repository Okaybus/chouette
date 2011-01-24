package fr.certu.chouette.modele;

import java.util.Date;

import chouette.schema.VehicleJourney;
import chouette.schema.types.ServiceStatusValueType;
import chouette.schema.types.TransportModeNameType;
import org.exolab.castor.types.Duration;

public class Course extends BaseObjet
{
	private VehicleJourney vehicleJourney;
	
	private Long idItineraire;
	private Long idMission;

	public Course() {
		super();
		
		vehicleJourney = new VehicleJourney();
	}

	public Long getIdMission() {
		return idMission;
	}

	public void setIdMission(Long idMission) {
		this.idMission = idMission;
	}

	public VehicleJourney getVehicleJourney() {
		return vehicleJourney;
	}

	public void setVehicleJourney( final VehicleJourney vehicleJourney) {
		this.vehicleJourney = vehicleJourney;
	}

	public Long getIdItineraire() {
		return idItineraire;
	}

	public void setIdItineraire( final Long idItineraire) {
		this.idItineraire = idItineraire;
	}

	public String getComment() {
		return vehicleJourney.getComment();
	}

	public Date getCreationTime() {
		return vehicleJourney.getCreationTime();
	}

	public String getCreatorId() {
		return vehicleJourney.getCreatorId();
	}

	public String getFacility() {
		return vehicleJourney.getFacility();
	}

	public String getObjectId() {
		return vehicleJourney.getObjectId();
	}

	public int getObjectVersion() {
		return (int)vehicleJourney.getObjectVersion();
	}

	public String getPublishedJourneyIdentifier() {
		return vehicleJourney.getPublishedJourneyIdentifier();
	}

	public String getPublishedJourneyName() {
		return vehicleJourney.getPublishedJourneyName();
	}

	public ServiceStatusValueType getStatusValue() {
		return vehicleJourney.getStatusValue();
	}

	public String getVehicleTypeIdentifier() {
		return vehicleJourney.getVehicleTypeIdentifier();
	}

	public TransportModeNameType getTransportMode() {
		return vehicleJourney.getTransportMode();
	}

	public int getNumber() {
		return (int)vehicleJourney.getNumber();
	}

	public String getRouteId() {
		return vehicleJourney.getRouteId();
	}

	public String getJourneyPatternId() {
		return vehicleJourney.getJourneyPatternId();
	}

	public void setJourneyPatternId(String journeyPatternId) {
		vehicleJourney.setJourneyPatternId(journeyPatternId);
	}

	public void setRouteId(String routeId) {
		vehicleJourney.setRouteId(routeId);
	}

	public void setNumber(int number) {
		vehicleJourney.setNumber(number);
	}

	public void setTransportMode(TransportModeNameType transportMode) {
		vehicleJourney.setTransportMode(transportMode);
	}

	public void setComment(String comment) {
		vehicleJourney.setComment(comment);
	}

	public void setCreationTime(Date creationTime) {
		vehicleJourney.setCreationTime(creationTime);
	}

	public void setCreatorId(String creatorId) {
		vehicleJourney.setCreatorId(creatorId);
	}

	public void setFacility(String facility) {
		vehicleJourney.setFacility(facility);
	}

	public void setObjectId(String objectId) {
		vehicleJourney.setObjectId(objectId);
	}

	public void setObjectVersion(int objectVersion) {
		vehicleJourney.setObjectVersion(objectVersion);
	}

	public void setPublishedJourneyIdentifier(String publishedJourneyIdentifier) {
		vehicleJourney.setPublishedJourneyIdentifier(publishedJourneyIdentifier);
	}

	public void setPublishedJourneyName(String publishedJourneyName) {
		vehicleJourney.setPublishedJourneyName(publishedJourneyName);
	}

	public void setStatusValue(ServiceStatusValueType statusValue) {
		vehicleJourney.setStatusValue(statusValue);
	}

	public void setVehicleTypeIdentifier(String vehicleTypeIdentifier) {
		vehicleJourney.setVehicleTypeIdentifier(vehicleTypeIdentifier);
	}

    public Long getVehicleTimeSlotIdentifier() {
        if (vehicleJourney.getTimeSlotId() != null)
            return Long.decode(vehicleJourney.getTimeSlotId());
        return null;
    }

    public void setVehicleTimeSlotIdentifier(Long vehicleTimeSlotIdentifier) {
        vehicleJourney.setTimeSlotId(vehicleTimeSlotIdentifier.toString());
    }

    public Duration getVehicleHeadwayFrequency() {
        if (vehicleJourney.getVehicleJourneyAtStopCount() > 0)
            if (vehicleJourney.getVehicleJourneyAtStop()[0].getHeadwayFrequency() != null)
                return vehicleJourney.getVehicleJourneyAtStop()[0].getHeadwayFrequency();
        return null;
    }

    public void setVehicleHeadwayFrequency(Duration vehicleHeadwayFrequency) {
        for (int i = 0; i < vehicleJourney.getVehicleJourneyAtStopCount(); i++)
            vehicleJourney.getVehicleJourneyAtStop()[i].setHeadwayFrequency(vehicleHeadwayFrequency);
    }
	
}
