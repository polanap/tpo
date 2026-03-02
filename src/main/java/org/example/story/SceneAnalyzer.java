package org.example.story;

import java.util.ArrayList;
import java.util.List;

public final class SceneAnalyzer {

    public ObservationReport analyze(TwoHeadedPerson person) {
        List<AnomalyType> anomalies = new ArrayList<>();

        if (person.hasTwoHeads()) {
            anomalies.add(AnomalyType.TWO_HEADS);
        }
        if (person.hasFeetOnControlPanel()) {
            anomalies.add(AnomalyType.FEET_ON_CONTROL_PANEL);
        }
        if (person.isLeftHandPicksRightHeadTeeth()) {
            anomalies.add(AnomalyType.LEFT_HAND_PICKS_RIGHT_HEAD_TEETH);
        }
        if (person.rightHeadState() == HeadState.BUSY_WITH_TEETH_PICKING
                && person.leftHeadState() == HeadState.SMILING_WIDE_RELAXED) {
            anomalies.add(AnomalyType.RIGHT_HEAD_BUSY_LEFT_HEAD_SMILING);
        }

        return new ObservationReport(anomalies);
    }
}
