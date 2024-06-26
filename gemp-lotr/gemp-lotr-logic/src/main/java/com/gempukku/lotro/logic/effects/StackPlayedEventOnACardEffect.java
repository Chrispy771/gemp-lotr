package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collections;

public class StackPlayedEventOnACardEffect extends AbstractEffect {
    private final PlayEventAction _action;
    private final PhysicalCard _stackOn;

    public StackPlayedEventOnACardEffect(PlayEventAction action, PhysicalCard stackOn) {
        _action = action;
        _stackOn = stackOn;
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack " + GameUtils.getFullName(_action.getEventPlayed()) + " on "+GameUtils.getFullName(_stackOn);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        Zone zone = _action.getEventPlayed().getZone();
        return _stackOn.getZone().isInPlay() && (zone == Zone.VOID || zone == Zone.VOID_FROM_HAND);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            PhysicalCard eventPlayed = _action.getEventPlayed();
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " stacks " + GameUtils.getCardLink(eventPlayed) + " on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().removeCardsFromZone(eventPlayed.getOwner(), Collections.singletonList(eventPlayed));
            game.getGameState().stackCard(game, eventPlayed, _stackOn);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}