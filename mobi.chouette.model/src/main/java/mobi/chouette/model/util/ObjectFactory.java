package mobi.chouette.model.util;

import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.Company;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.PTNetwork;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;

public class ObjectFactory {

	public static AccessLink getAccessLink(Referential referential,
			String objectId) {
		AccessLink result = referential.getAccessLinks().get(objectId);
		if (result == null) {
			result = new AccessLink();
			result.setObjectId(objectId);
			referential.getAccessLinks().put(objectId, result);
		}
		return result;
	}

	public static AccessPoint getAccessPoint(Referential referential,
			String objectId) {
		AccessPoint result = referential.getAccessPoints().get(objectId);
		if (result == null) {
			result = new AccessPoint();
			result.setObjectId(objectId);
			referential.getAccessPoints().put(objectId, result);
		}
		return result;
	}

	public static Timetable getTimetable(Referential referential,
			String objectId) {
		Timetable result = referential.getTimetables().get(objectId);
		if (result == null) {
			result = new Timetable();
			result.setObjectId(objectId);
			referential.getTimetables().put(objectId, result);
		}
		return result;
	}

	public static VehicleJourneyAtStop getVehicleJourneyAtStop() {
		// TODO [DSU] object pool
		VehicleJourneyAtStop result = new VehicleJourneyAtStop();
		return result;
	}

	public static PTNetwork getPTNetwork(Referential referential,
			String objectId) {
		PTNetwork result = referential.getPtNetworks().get(objectId);
		if (result == null) {
			result = new PTNetwork();
			result.setObjectId(objectId);
			referential.getPtNetworks().put(objectId, result);
		}
		return result;
	}

	public static Company getCompany(Referential referential, String objectId) {
		Company result = referential.getCompanies().get(objectId);
		if (result == null) {
			result = new Company();
			result.setObjectId(objectId);
			referential.getCompanies().put(objectId, result);
		}
		return result;
	}

	public static Route getRoute(Referential referential, String objectId) {
		Route result = referential.getRoutes().get(objectId);
		if (result == null) {
			result = new Route();
			result.setObjectId(objectId);
			referential.getRoutes().put(objectId, result);
		}
		return result;
	}

	public static Line getLine(Referential referential, String objectId) {
		Line result = referential.getLines().get(objectId);
		if (result == null) {
			result = new Line();
			result.setObjectId(objectId);
			referential.getLines().put(objectId, result);
		}
		return result;
	}

	public static JourneyPattern getJourneyPattern(Referential referential,
			String objectId) {
		JourneyPattern result = referential.getJourneyPatterns().get(objectId);
		if (result == null) {
			result = new JourneyPattern();
			result.setObjectId(objectId);
			referential.getJourneyPatterns().put(objectId, result);
		}
		return result;
	}

	public static ConnectionLink getConnectionLink(Referential referential,
			String objectId) {
		ConnectionLink result = referential.getConnectionLinks().get(objectId);
		if (result == null) {
			result = new ConnectionLink();
			result.setObjectId(objectId);
			referential.getConnectionLinks().put(objectId, result);
		}
		return result;
	}

	public static StopArea getStopArea(Referential referential, String objectId) {
		StopArea result = referential.getStopAreas().get(objectId);
		if (result == null) {
			result = new StopArea();
			result.setObjectId(objectId);
			referential.getStopAreas().put(objectId, result);
		}
		return result;
	}

	public static GroupOfLine getGroupOfLine(Referential referential,
			String objectId) {
		GroupOfLine result = referential.getGroupOfLines().get(objectId);
		if (result == null) {
			result = new GroupOfLine();
			result.setObjectId(objectId);
			referential.getGroupOfLines().put(objectId, result);
		}
		return result;
	}

	public static StopPoint getStopPoint(Referential referential,
			String objectId) {
		StopPoint result = referential.getStopPoints().get(objectId);
		if (result == null) {
			result = new StopPoint();
			result.setObjectId(objectId);
			referential.getStopPoints().put(objectId, result);
		}
		return result;
	}

	public static VehicleJourney getVehicleJourney(Referential referential,
			String objectId) {
		VehicleJourney result = referential.getVehicleJourneys().get(objectId);
		if (result == null) {
			result = new VehicleJourney();
			result.setObjectId(objectId);
			referential.getVehicleJourneys().put(objectId, result);
		}
		return result;
	}

}
