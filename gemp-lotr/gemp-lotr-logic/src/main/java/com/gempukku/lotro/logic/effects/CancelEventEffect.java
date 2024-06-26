package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

public class CancelEventEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final PlayEventResult _effect;

    public CancelEventEffect(PhysicalCard source, PlayEventResult effectResult) {
        _source = source;
        _effect = effectResult;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return !_effect.isEventCancelled();
    }

    @Override
    public String getText(LotroGame game) {
        return "Cancel effect - " + GameUtils.getFullName(_effect.getPlayedCard());
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " cancels effect - " + GameUtils.getCardLink(_effect.getPlayedCard()));
            _effect.cancelEvent();
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
