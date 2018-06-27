package ch.ethz.matsim.mode_choice.v2.framework.trip_based.constraints;

import java.util.List;

import ch.ethz.matsim.mode_choice.v2.framework.ModeChoiceTrip;
import ch.ethz.matsim.mode_choice.v2.framework.trip_based.estimation.TripCandidate;

public interface TripConstraint {
	boolean validateBeforeEstimation(ModeChoiceTrip trip, String mode, List<String> previousModes);

	boolean validateAfterEstimation(ModeChoiceTrip trip, TripCandidate candidate,
			List<TripCandidate> previousCandidates);
}