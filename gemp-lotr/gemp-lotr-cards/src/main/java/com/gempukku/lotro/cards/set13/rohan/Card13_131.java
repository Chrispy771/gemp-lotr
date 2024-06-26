package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Exert Eomer to make Eowyn or Theoden strength +2. Skirmish: Exert Eowyn to make Eomer or Theoden
 * strength +2. Skirmish: Exert Theoden to make Eomer or Eowyn strength +2.
 */
public class Card13_131 extends AbstractPermanent {
    public Card13_131() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, "King's Board", null, true);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            List<ActivateCardAction> actions = new LinkedList<>();
            if (PlayConditions.canExert(self, game, Filters.name(Names.eomer))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Exert Eomer...");
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name(Names.eomer)));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.or(Filters.name(Names.eowyn), Filters.name(Names.theoden))));
                actions.add(action);
            }
            if (PlayConditions.canExert(self, game, Filters.name(Names.eowyn))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Exert Eowyn...");
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name(Names.eowyn)));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.or(Filters.name(Names.eomer), Filters.name(Names.theoden))));
                actions.add(action);
            }
            if (PlayConditions.canExert(self, game, Filters.name(Names.theoden))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Exert Theoden...");
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name(Names.theoden)));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.or(Filters.name(Names.eowyn), Filters.name(Names.eomer))));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
