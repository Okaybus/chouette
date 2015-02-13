package mobi.chouette.exchange.importer.updater;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.CollectionUtils;
import mobi.chouette.common.Context;
import mobi.chouette.common.Pair;
import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.PTNetwork;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = RouteUpdater.BEAN_NAME)
public class RouteUpdater implements Updater<Route> {

	public static final String BEAN_NAME = "RouteUpdater";

	@EJB
	private RouteDAO routeDAO;

	@EJB
	private StopPointDAO stopPointDAO;

	@EJB(beanName = StopPointUpdater.BEAN_NAME)
	private Updater<StopPoint> stopPointUpdater;

	@EJB
	private JourneyPatternDAO journeyPatternDAO;

	@EJB(beanName = JourneyPatternUpdater.BEAN_NAME)
	private Updater<JourneyPattern> journeyPatternUpdater;

	@Override
	public void update(Context context, Route oldValue, Route newValue)
			throws Exception {

		if (newValue.isSaved()) {
			return;
		}
		newValue.setSaved(true);

		Referential cache = (Referential) context.get(CACHE);

		if (newValue.getObjectId() != null
				&& !newValue.getObjectId().equals(oldValue.getObjectId())) {
			oldValue.setObjectId(newValue.getObjectId());
		}
		if (newValue.getObjectVersion() != null
				&& !newValue.getObjectVersion().equals(
						oldValue.getObjectVersion())) {
			oldValue.setObjectVersion(newValue.getObjectVersion());
		}
		if (newValue.getCreationTime() != null
				&& !newValue.getCreationTime().equals(
						oldValue.getCreationTime())) {
			oldValue.setCreationTime(newValue.getCreationTime());
		}
		if (newValue.getCreatorId() != null
				&& !newValue.getCreatorId().equals(oldValue.getCreatorId())) {
			oldValue.setCreatorId(newValue.getCreatorId());
		}
		if (newValue.getName() != null
				&& !newValue.getName().equals(oldValue.getName())) {
			oldValue.setName(newValue.getName());
		}
		if (newValue.getComment() != null
				&& !newValue.getComment().equals(oldValue.getComment())) {
			oldValue.setComment(newValue.getComment());
		}
		if (newValue.getPublishedName() != null
				&& !newValue.getPublishedName().equals(
						oldValue.getPublishedName())) {
			oldValue.setPublishedName(newValue.getPublishedName());
		}
		if (newValue.getNumber() != null
				&& !newValue.getNumber().equals(oldValue.getNumber())) {
			oldValue.setNumber(newValue.getNumber());
		}
		if (newValue.getDirection() != null
				&& !newValue.getDirection().equals(oldValue.getDirection())) {
			oldValue.setDirection(newValue.getDirection());
		}
		if (newValue.getWayBack() != null
				&& !newValue.getWayBack().equals(oldValue.getWayBack())) {
			oldValue.setWayBack(newValue.getWayBack());
		}

		// OppositeRoute
		if (newValue.getOppositeRoute() != null) {

			String objectId = newValue.getOppositeRoute().getObjectId();
			Route opposite = cache.getRoutes().get(objectId);
			if (opposite == null) {
				opposite = routeDAO.findByObjectId(objectId);
				if (opposite != null) {
					cache.getRoutes().put(objectId, opposite);
				}
			}
			if (opposite != null) {
				oldValue.setOppositeRoute(opposite);
			}
		}

		// StopPoint
		Collection<StopPoint> addedStopPoint = CollectionUtils.substract(
				newValue.getStopPoints(), oldValue.getStopPoints(),
				NeptuneIdentifiedObjectComparator.INSTANCE);

		List<StopPoint> stopPoints = null;
		for (StopPoint item : addedStopPoint) {

			StopPoint stopPoint = cache.getStopPoints().get(item.getObjectId());
			if (stopPoint == null) {
				if (stopPoints == null) {
					stopPoints = stopPointDAO.load(addedStopPoint);
					for (StopPoint object : stopPoints) {
						cache.getStopPoints().put(object.getObjectId(), object);
					}
				}
				stopPoint = cache.getStopPoints().get(item.getObjectId());
			}

			if (stopPoint == null) {
				stopPoint = ObjectFactory.getStopPoint(cache,
						item.getObjectId());
				// stopPoint.setObjectId(item.getObjectId());
			}
			stopPoint.setRoute(oldValue);
		}

		// Updater<StopPoint> stopPointUpdater = UpdaterFactory.create(
		// initialContext, StopPointUpdater.class.getName());
		Collection<Pair<StopPoint, StopPoint>> modifiedStopPoint = CollectionUtils
				.intersection(oldValue.getStopPoints(),
						newValue.getStopPoints(),
						NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<StopPoint, StopPoint> pair : modifiedStopPoint) {
			stopPointUpdater.update(context, pair.getLeft(), pair.getRight());
		}

		Collection<StopPoint> removedStopPoint = CollectionUtils.substract(
				oldValue.getStopPoints(), newValue.getStopPoints(),
				NeptuneIdentifiedObjectComparator.INSTANCE);
		for (StopPoint stopPoint : removedStopPoint) {
			stopPoint.setRoute(null);
			stopPointDAO.delete(stopPoint);
		}

		// JourneyPattern
		Collection<JourneyPattern> addedJourneyPattern = CollectionUtils
				.substract(newValue.getJourneyPatterns(),
						oldValue.getJourneyPatterns(),
						NeptuneIdentifiedObjectComparator.INSTANCE);

		List<JourneyPattern> journeyPatterns = null;
		for (JourneyPattern item : addedJourneyPattern) {

			JourneyPattern journeyPattern = cache.getJourneyPatterns().get(
					item.getObjectId());
			if (journeyPattern == null) {
				if (journeyPatterns == null) {
					journeyPatterns = journeyPatternDAO
							.load(addedJourneyPattern);
					for (JourneyPattern object : journeyPatterns) {
						cache.getJourneyPatterns().put(object.getObjectId(),
								object);
					}
				}
				journeyPattern = cache.getJourneyPatterns().get(
						item.getObjectId());
			}

			if (journeyPattern == null) {
				journeyPattern = ObjectFactory.getJourneyPattern(cache,
						item.getObjectId());
				// journeyPattern.setObjectId(item.getObjectId());
			}
			journeyPattern.setRoute(oldValue);
		}

		// Updater<JourneyPattern> journeyPatternUpdater = //
		// UpdaterFactory.create(
		// initialContext, JourneyPatternUpdater.class.getName());
		Collection<Pair<JourneyPattern, JourneyPattern>> modifiedJourneyPattern = CollectionUtils
				.intersection(oldValue.getJourneyPatterns(),
						newValue.getJourneyPatterns(),
						NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<JourneyPattern, JourneyPattern> pair : modifiedJourneyPattern) {
			journeyPatternUpdater.update(context, pair.getLeft(),
					pair.getRight());
		}

		// Collection<JourneyPattern> removedJourneyPattern = CollectionUtils
		// .substract(oldValue.getJourneyPatterns(),
		// newValue.getJourneyPatterns(),
		// NeptuneIdentifiedObjectComparator.INSTANCE);
		// for (JourneyPattern journeyPattern : removedJourneyPattern) {
		// journeyPattern.setRoute(null);
		// journeyPatternDAO.delete(journeyPattern);
		// }

	}

	static {
		UpdaterFactory.register(RouteUpdater.class.getName(),
				new UpdaterFactory() {

					@Override
					protected <T> Updater<T> create(InitialContext context) {
						Updater result = null;
						try {
							result = (Updater) context
									.lookup("java:app/mobi.chouette.exchange/"
											+ BEAN_NAME);
						} catch (NamingException e) {
							log.error(e);
						}
						return result;
					}
				});
	}

}
