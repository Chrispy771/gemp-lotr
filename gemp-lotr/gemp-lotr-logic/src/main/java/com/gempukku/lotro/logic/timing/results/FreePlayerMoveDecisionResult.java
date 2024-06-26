package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class FreePlayerMoveDecisionResult extends EffectResult {

    private final boolean _isMoving;
    public FreePlayerMoveDecisionResult(boolean moving) {
        super(Type.FREE_PEOPLE_PLAYER_DECIDED_IF_MOVING);
        _isMoving = moving;
    }

    public boolean IsMoving() { return _isMoving; }
    public boolean IsStaying() { return !_isMoving; }
}
