package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SpotEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsFromLosingSkirmishModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Fortification. Characters skirmishing bearer cannot take wounds from losing a skirmish involving bearer.
 * Skirmish: Exert a [GONDOR] Man or spot 3 rangers to transfer this condition from your support area to a minion
 * skirmishing a [GONDOR] Man.
 */
public class Card15_070 extends AbstractPermanent {
    public Card15_070() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, "Tremendous Wall", null, true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new CantTakeWoundsFromLosingSkirmishModifier(self,
Filters.and(Filters.character, Filters.inSkirmishAgainst(Filters.hasAttached(self))), Filters.hasAttached(self)));
}

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && (PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN)
                || PlayConditions.canSpot(game, 3, Keyword.RANGER))
                && self.getZone() == Zone.SUPPORT) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a GONDOR Man";
                        }
                    });
            possibleCosts.add(
                    new SpotEffect(3, Keyword.RANGER) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Spot 3 rangers";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new TransferPermanentEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
