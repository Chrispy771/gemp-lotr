package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class RevealCardFromHandResult extends EffectResult {
    private final PhysicalCard _source;
    private final String _playerId;
    private final PhysicalCard _card;

    public RevealCardFromHandResult(PhysicalCard source, String playerId, PhysicalCard card) {
        super(Type.FOR_EACH_REVEALED_FROM_HAND);
        _source = source;
        _playerId = playerId;
        _card = card;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public PhysicalCard getRevealedCard() {
        return _card;
    }
}
