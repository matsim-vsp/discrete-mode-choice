package ch.ethz.matsim.discrete_mode_choice.components.tour_finder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.StageActivityTypesImpl;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.router.TripStructureUtils.Trip;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class ActivityTourFinderTest {
	private List<DiscreteModeChoiceTrip> createFixture(String... activityTypes) {
		Plan plan = PopulationUtils.createPlan();
		boolean isFirst = true;

		for (String activityType : activityTypes) {
			if (!isFirst) {
				PopulationUtils.createAndAddLeg(plan, "");
			}

			PopulationUtils.createAndAddActivity(plan, activityType);
			isFirst = false;
		}

		List<Trip> trips = TripStructureUtils.getTrips(plan, new StageActivityTypesImpl());
		List<DiscreteModeChoiceTrip> modeChoiceTrips = new LinkedList<>();

		for (Trip trip : trips) {
			String initialMode = trip.getLegsOnly().get(0).getMode();
			modeChoiceTrips.add(new DiscreteModeChoiceTrip(trip.getOriginActivity(), trip.getDestinationActivity(),
					initialMode, 0.0));
		}

		return modeChoiceTrips;
	}

	@Test
	public void testActivityTourFinder() {
		ActivityTourFinder finder = new ActivityTourFinder("home");

		List<DiscreteModeChoiceTrip> trips;
		List<List<DiscreteModeChoiceTrip>> result;

		trips = createFixture("home", "work", "home");
		result = finder.findTours(trips);
		assertEquals(1, result.size());
		assertEquals(2, result.stream().mapToInt(List::size).sum());

		trips = createFixture("other", "home", "work", "home");
		result = finder.findTours(trips);
		assertEquals(2, result.size());
		assertEquals(3, result.stream().mapToInt(List::size).sum());
		assertEquals(1, result.get(0).size());
		assertEquals(2, result.get(1).size());

		trips = createFixture("home", "work", "home", "other");
		result = finder.findTours(trips);
		assertEquals(2, result.size());
		assertEquals(3, result.stream().mapToInt(List::size).sum());
		assertEquals(2, result.get(0).size());
		assertEquals(1, result.get(1).size());

		trips = createFixture("home", "work", "shop", "home", "other", "home");
		result = finder.findTours(trips);
		assertEquals(2, result.size());
		assertEquals(5, result.stream().mapToInt(List::size).sum());
		assertEquals(3, result.get(0).size());
		assertEquals(2, result.get(1).size());

		trips = createFixture("home", "work", "home", "home", "work", "home");
		result = finder.findTours(trips);
		assertEquals(3, result.size());
		assertEquals(5, result.stream().mapToInt(List::size).sum());
		assertEquals(2, result.get(0).size());
		assertEquals(1, result.get(1).size());
		assertEquals(2, result.get(2).size());
	}
}
