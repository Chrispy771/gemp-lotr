package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class RemoveCharacterFromSkirmishEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final PhysicalCard _toRemove;

    public RemoveCharacterFromSkirmishEffect(PhysicalCard source, PhysicalCard toRemove) {
        _source = source;
        _toRemove = toRemove;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.inSkirmish.accepts(game, _toRemove);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().removeFromSkirmish(_toRemove);
        }
        return new FullEffectResult(false);
    }
}
