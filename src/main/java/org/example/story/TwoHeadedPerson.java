package org.example.story;

public final class TwoHeadedPerson {
    private final boolean twoHeads;
    private final boolean reclinedInArmchair;
    private final boolean feetOnControlPanel;
    private final boolean leftHandPicksRightHeadTeeth;
    private final HeadState leftHeadState;
    private final HeadState rightHeadState;

    public TwoHeadedPerson(
            boolean twoHeads,
            boolean reclinedInArmchair,
            boolean feetOnControlPanel,
            boolean leftHandPicksRightHeadTeeth,
            HeadState leftHeadState,
            HeadState rightHeadState
    ) {
        this.twoHeads = twoHeads;
        this.reclinedInArmchair = reclinedInArmchair;
        this.feetOnControlPanel = feetOnControlPanel;
        this.leftHandPicksRightHeadTeeth = leftHandPicksRightHeadTeeth;
        this.leftHeadState = leftHeadState;
        this.rightHeadState = rightHeadState;
    }

    public static TwoHeadedPerson fromTextScene() {
        return new TwoHeadedPerson(
                true,
                true,
                true,
                true,
                HeadState.SMILING_WIDE_RELAXED,
                HeadState.BUSY_WITH_TEETH_PICKING
        );
    }

    public boolean isReclinedInArmchair() {
        return reclinedInArmchair;
    }

    public boolean hasFeetOnControlPanel() {
        return feetOnControlPanel;
    }

    public boolean isLeftHandPicksRightHeadTeeth() {
        return leftHandPicksRightHeadTeeth;
    }

    public HeadState leftHeadState() {
        return leftHeadState;
    }

    public HeadState rightHeadState() {
        return rightHeadState;
    }

    public boolean hasTwoHeads() {
        return twoHeads;
    }
}
