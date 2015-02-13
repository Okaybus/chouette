package mobi.chouette.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang.StringUtils;

/**
 * Chouette Journey Pattern : pattern for vehicle journeys in a route
 * <p/>
 * Neptune mapping : JourneyPattern <br/>
 * Gtfs mapping : none
 * 
 */
@Entity
@Table(name = "journey_patterns")
@NoArgsConstructor
@ToString(callSuper=true, exclude = { "route" })
@Log4j
public class JourneyPattern extends NeptuneIdentifiedObject {
	private static final long serialVersionUID = 7895941111990419404L;

	/**
	 * name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "name")
	private String name;

	/**
	 * set name <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setName(String value) {
		name = StringUtils.abbreviate(value, 255);
	}

	/**
	 * comment
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "comment")
	private String comment;

	/**
	 * set comment <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setComment(String value) {
		comment = StringUtils.abbreviate(value, 255);
	}

	/**
	 * registration number
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "registration_number")
	private String registrationNumber;

	/**
	 * set registration number <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setRegistrationNumber(String value) {
		registrationNumber = StringUtils.abbreviate(value, 255);
	}

	/**
	 * published name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "published_name")
	private String publishedName;

	/**
	 * set published name <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setPublishedName(String value) {
		publishedName = StringUtils.abbreviate(value, 255);
	}

	/**
	 * route reverse reference
	 * 
	 * @param route
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id")
	private Route route;

	/**
	 * set route reverse reference
	 * 
	 * @param route
	 */
	public void setRoute(Route route) {
		if (this.route != null) {
			this.route.getJourneyPatterns().remove(this);
		}
		this.route = route;
		if (route != null) {
			route.getJourneyPatterns().add(this);
		}
	}

	/**
	 * first stop
	 * 
	 * @param departureStopPoint
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departure_stop_point_id")
	private StopPoint departureStopPoint;

	/**
	 * last stop
	 * 
	 * @param arrivalStopPoint
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "arrival_stop_point_id")
	private StopPoint arrivalStopPoint;

	/**
	 * stop list
	 * 
	 * @param stopPoints
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToMany
	// TODO [DSU] @OrderBy(value="position") : antlr.CommonToken cannot be cast to antlr.Token
	@JoinTable(name = "journey_patterns_stop_points", joinColumns = { @JoinColumn(name = "journey_pattern_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "stop_point_id", nullable = false, updatable = false) })
	private List<StopPoint> stopPoints = new ArrayList<StopPoint>(0);

	/**
	 * vehicle journeys
	 * 
	 * @param vehicleJourneys
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@OneToMany(mappedBy = "journeyPattern", cascade = { CascadeType.PERSIST})
	private List<VehicleJourney> vehicleJourneys = new ArrayList<VehicleJourney>(
			0);

	/**
	 * add a stop point if not already present
	 * 
	 * @param stopPoint
	 */
	public void addStopPoint(StopPoint stopPoint) {
		if (!stopPoints.contains(stopPoint)) {
			stopPoints.add(stopPoint);
		}
	}

	/**
	 * remove a stop point
	 * 
	 * @param stopPoint
	 */
	public void removeStopPoint(StopPoint stopPoint) {
		if (stopPoints.contains(stopPoint)) {
			stopPoints.remove(stopPoint);
		}
	}

}
