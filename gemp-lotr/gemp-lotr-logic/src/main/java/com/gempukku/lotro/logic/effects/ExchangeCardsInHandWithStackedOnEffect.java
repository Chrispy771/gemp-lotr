package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExchangeCardsInHandWithStackedOnEffect extends AbstractEffect {
    private final String _performingPlayer;
    private final PhysicalCard _source;
    private final String _playerHand;
    private final PhysicalCard _stackedOn;

    public ExchangeCardsInHandWithStackedOnEffect(String performingPlayer, PhysicalCard source, String playerHand, PhysicalCard stackedOn) {
        _performingPlayer = performingPlayer;
        _source = source;
        _playerHand = playerHand;
        _stackedOn = stackedOn;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            final List<PhysicalCard> stackedCards = new LinkedList<>(game.getGameState().getStackedCards(_stackedOn));
            final List<PhysicalCard> hand = new LinkedList<>(game.getGameState().getHand(_playerHand));

            Set<PhysicalCard> toRemove = new HashSet<>();
            toRemove.addAll(stackedCards);
            toRemove.addAll(hand);

            game.getGameState().removeCardsFromZone(_performingPlayer, toRemove);
            
            for (PhysicalCard cardInHand : hand)
                game.getGameState().stackCard(game, cardInHand, _stackedOn);

            for (PhysicalCard stackedCard : stackedCards)
                game.getGameState().addCardToZone(game, stackedCard, Zone.HAND);

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _stackedOn.getZone().isInPlay();
    }
}
