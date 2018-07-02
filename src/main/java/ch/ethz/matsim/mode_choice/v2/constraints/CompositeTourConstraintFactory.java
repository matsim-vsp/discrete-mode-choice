package ch.ethz.matsim.mode_choice.v2.constraints;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ch.ethz.matsim.mode_choice.v2.framework.ModeChoiceTrip;
import ch.ethz.matsim.mode_choice.v2.framework.tour_based.constraints.TourConstraint;
import ch.ethz.matsim.mode_choice.v2.framework.tour_based.constraints.TourConstraintFactory;

public class CompositeTourConstraintFactory implements TourConstraintFactory {
	final private List<TourConstraintFactory> factories;

	public CompositeTourConstraintFactory(List<TourConstraintFactory> factories) {
		this.factories = factories;
	}

	@Override
	public TourConstraint createConstraint(List<ModeChoiceTrip> trips, Collection<String> availableModes) {
		return new CompositeTourConstraint(
				factories.stream().map(f -> f.createConstraint(trips, availableModes)).collect(Collectors.toList()));
	}
}