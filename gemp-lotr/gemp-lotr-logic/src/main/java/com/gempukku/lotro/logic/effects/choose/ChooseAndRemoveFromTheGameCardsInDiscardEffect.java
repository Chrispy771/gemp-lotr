package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.RemoveCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.List;

public class ChooseAndRemoveFromTheGameCardsInDiscardEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final PhysicalCard _source;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final Filterable[] _filters;
    private CostToEffectAction _resultSubAction;
    private boolean _success;

    public ChooseAndRemoveFromTheGameCardsInDiscardEffect(Action action, PhysicalCard source, String playerId, int minimum, int maximum, Filterable... filters) {
        _action = action;
        _source = source;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game, _filters).size() >= _minimum;
    }

    @Override
    public void playEffect(final LotroGame game) {
        final Collection<PhysicalCard> possibleTargets = Filters.filter(game.getGameState().getDiscard(_playerId), game, _filters);

        if (possibleTargets.size() <= _minimum) {
            processForCards(game, possibleTargets);
        } else {
            int min = _minimum;
            int max = Math.min(_maximum, possibleTargets.size());
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose cards to remove from the game", possibleTargets, min, max) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            processForCards(game, selectedCards);
                        }
                    });
        }
    }

    private void processForCards(LotroGame game, Collection<PhysicalCard> cards) {
        _resultSubAction = new SubAction(_action);
        _resultSubAction.appendEffect(
                new RemoveCardsFromDiscardEffect(_playerId, _source, cards));
        processSubAction(game, _resultSubAction);
        _success = cards.size() >= _minimum;
    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _success;
    }
}
