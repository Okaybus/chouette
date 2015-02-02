package mobi.chouette.exchange.neptune.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.importer.XPPUtil;
import mobi.chouette.exchange.neptune.importer.Constant;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.ChouetteAreaEnum;
import mobi.chouette.model.type.LongLatTypeEnum;
import mobi.chouette.model.type.UserNeedEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import org.xmlpull.v1.XmlPullParser;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

@Log4j
public class ChouetteAreaParser implements Parser, Constant {
	private static final String CHILD_TAG = "ChouetteArea";

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(XPP);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, CHILD_TAG);
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		BiMap<String, String> map = HashBiMap.create();

		while (xpp.nextTag() == XmlPullParser.START_TAG) {

			if (xpp.getName().equals("StopArea")) {
				parseStopArea(context, map);
			} else if (xpp.getName().equals("AreaCentroid")) {
				parseAreaCentroid(context, map);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseStopArea(Context context, BiMap<String, String> map)
			throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(XPP);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, "StopArea");
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		StopArea stopArea = null;
		List<String> contains = new ArrayList<String>();

		while (xpp.nextTag() == XmlPullParser.START_TAG) {

			if (xpp.getName().equals("objectId")) {
				String objectId = ParserUtils.getText(xpp.nextText());
				stopArea = ObjectFactory.getStopArea(referential, objectId);
			} else if (xpp.getName().equals("objectVersion")) {
				Integer version = ParserUtils.getInt(xpp.nextText());
				stopArea.setObjectVersion(version);
			} else if (xpp.getName().equals("creationTime")) {
				Date creationTime = ParserUtils.getSQLDateTime(xpp.nextText());
				stopArea.setCreationTime(creationTime);
			} else if (xpp.getName().equals("creatorId")) {
				stopArea.setCreatorId(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("name")) {
				stopArea.setName(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("comment")) {
				stopArea.setComment(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("StopAreaExtension")) {

				while (xpp.nextTag() == XmlPullParser.START_TAG) {

					if (xpp.getName().equals("areaType")) {
						stopArea.setAreaType(ParserUtils.getEnum(
								ChouetteAreaEnum.class, xpp.nextText()));
						if (stopArea.getAreaType() == ChouetteAreaEnum.BoardingPosition
								|| stopArea.getAreaType() == ChouetteAreaEnum.Quay) {
							for (String objectId : contains) {
								StopPoint stopPoint = ObjectFactory
										.getStopPoint(referential, objectId);
								stopPoint.setContainedInStopArea(stopArea);
							}
						} else {
							for (String objectId : contains) {
								StopArea child = ObjectFactory.getStopArea(
										referential, objectId);
								child.setParent(stopArea);
							}
						}
					} else if (xpp.getName().equals("nearestTopicName")) {
						stopArea.setNearestTopicName(ParserUtils.getText(xpp
								.nextText()));
					} else if (xpp.getName().equals("fareCode")) {
						stopArea.setFareCode(ParserUtils.getInt(xpp.nextText()));
					} else if (xpp.getName().equals("registration")) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals("registrationNumber")) {
								stopArea.setRegistrationNumber(ParserUtils
										.getText(xpp.nextText()));
							} else {
								XPPUtil.skipSubTree(log, xpp);
							}
						}
					} else if (xpp.getName().equals(
							"mobilityRestrictedSuitability")) {
						stopArea.setMobilityRestrictedSuitable(ParserUtils
								.getBoolean(xpp.nextText()));
					} else if (xpp.getName().equals(
							"accessibilitySuitabilityDetails")) {
						List<UserNeedEnum> userNeeds = new ArrayList<UserNeedEnum>();
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals("MobilityNeed")
									|| xpp.getName()
											.equals("PsychosensoryNeed")
									|| xpp.getName().equals("MedicalNeed")
									|| xpp.getName().equals("EncumbranceNeed")) {
								UserNeedEnum userNeed = ParserUtils.getEnum(
										UserNeedEnum.class, xpp.nextText());
								if (userNeed != null) {
									userNeeds.add(userNeed);
								}

							} else {
								XPPUtil.skipSubTree(log, xpp);
							}
						}
						stopArea.setUserNeeds(userNeeds);
					} else if (xpp.getName().equals("stairsAvailability")) {
						stopArea.setStairsAvailable(ParserUtils.getBoolean(xpp
								.nextText()));
					} else if (xpp.getName().equals("liftAvailability")) {
						stopArea.setLiftAvailable(ParserUtils.getBoolean(xpp
								.nextText()));
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else if (xpp.getName().equals("contains")) {
				contains.add(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("centroidOfArea")) {
				String key = stopArea.getObjectId();
				String value = ParserUtils.getText(xpp.nextText());
				map.put(key, value);

			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseAreaCentroid(Context context, BiMap<String, String> map)
			throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(XPP);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, "AreaCentroid");
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		BiMap<String, String> inverse = map.inverse();
		StopArea stopArea = null;

		while (xpp.nextTag() == XmlPullParser.START_TAG) {

			if (xpp.getName().equals("objectId")) {
				String objectId = inverse.get(ParserUtils.getText(xpp
						.nextText()));
				stopArea = ObjectFactory.getStopArea(referential, objectId);
			} else if (xpp.getName().equals("name")) {
				stopArea.setName(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("comment")) {
				stopArea.setComment(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("longLatType")) {
				stopArea.setLongLatType(ParserUtils.getEnum(
						LongLatTypeEnum.class, xpp.nextText()));
			} else if (xpp.getName().equals("latitude")) {
				stopArea.setLatitude(ParserUtils.getBigDecimal(xpp.nextText()));
			} else if (xpp.getName().equals("longitude")) {
				stopArea.setLongitude(ParserUtils.getBigDecimal(xpp.nextText()));
			} else if (xpp.getName().equals("containedIn")) {
				String objectId = ParserUtils.getText(xpp.nextText());
			} else if (xpp.getName().equals("address")) {

				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals("countryCode")) {
						stopArea.setCountryCode(ParserUtils.getText(xpp
								.nextText()));
					} else if (xpp.getName().equals("streetName")) {
						stopArea.setStreetName(ParserUtils.getText(xpp
								.nextText()));
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else if (xpp.getName().equals("projectedPoint")) {

				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals("X")) {
						BigDecimal value = ParserUtils.getBigDecimal(xpp
								.nextText());
						stopArea.setX(value);
					} else if (xpp.getName().equals("Y")) {
						BigDecimal value = ParserUtils.getBigDecimal(xpp
								.nextText());
						stopArea.setY(value);
					} else if (xpp.getName().equals("projectionType")) {
						String value = ParserUtils.getText(xpp.nextText());
						stopArea.setProjectionType(value);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	static {
		ParserFactory.register(ChouetteAreaParser.class.getName(),
				new ParserFactory() {
					private ChouetteAreaParser instance = new ChouetteAreaParser();

					@Override
					protected Parser create() {
						return instance;
					}
				});
	}
}