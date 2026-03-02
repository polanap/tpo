package org.example.story;

public final class Arthur {
    private boolean nervous;
    private boolean enteredFollowing;
    private boolean astonished;
    private boolean jawDropped;
    private int unbelievableThingsCount;

    public Arthur() {
        this.nervous = true;
    }

    public void enterFollowing() {
        enteredFollowing = true;
    }

    public void observe(ObservationReport report) {
        unbelievableThingsCount += report.anomalyCount();
        if (report.hasAnomalies()) {
            astonished = true;
            jawDropped = true;
        }
    }

    public boolean isNervous() {
        return nervous;
    }

    public boolean hasEnteredFollowing() {
        return enteredFollowing;
    }

    public boolean isAstonished() {
        return astonished;
    }

    public boolean isJawDropped() {
        return jawDropped;
    }

    public int unbelievableThingsCount() {
        return unbelievableThingsCount;
    }
}
