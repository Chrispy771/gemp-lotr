package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class StackTopCardsFromDeckEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerId;
    private final int _count;
    private final PhysicalCard _target;

    public StackTopCardsFromDeckEffect(PhysicalCard source, String playerId, int count, PhysicalCard target) {
        _source = source;
        _playerId = playerId;
        _count = count;
        _target = target;
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
    public boolean isPlayableInFull(LotroGame game) {
        return _target.getZone().isInPlay() && game.getGameState().getDeck(_playerId).size() >= _count;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_target.getZone().isInPlay()) {
            int stacked = 0;
            for (int i = 0; i < _count; i++) {
                final PhysicalCard card = game.getGameState().removeTopDeckCard(_playerId);
                if (card != null) {
                    game.getGameState().stackCard(game, card, _target);
                    stacked++;
                }
            }
            return new FullEffectResult(stacked == _count);
        }
        return new FullEffectResult(false);
    }
}
