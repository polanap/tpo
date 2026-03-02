package org.example.story;

import java.util.List;

public final class ObservationReport {
    private final List<AnomalyType> anomalies;

    public ObservationReport(List<AnomalyType> anomalies) {
        this.anomalies = List.copyOf(anomalies);
    }

    public List<AnomalyType> anomalies() {
        return anomalies;
    }

    public int anomalyCount() {
        return anomalies.size();
    }

    public boolean hasAnomalies() {
        return !anomalies.isEmpty();
    }
}
