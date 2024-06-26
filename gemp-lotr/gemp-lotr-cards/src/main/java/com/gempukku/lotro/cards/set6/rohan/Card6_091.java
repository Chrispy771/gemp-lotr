package com.gempukku.lotro.cards.set6.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.*;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Discard a mount borne by a [ROHAN] Man to prevent that Man from
 * being overwhelmed unless his or her strength is tripled.
 */
public class Card6_091 extends AbstractPermanent {
    public Card6_091() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, "Blood Has Been Spilled");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, PossessionClass.MOUNT, Filters.attachedTo(Culture.ROHAN, Race.MAN))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, PossessionClass.MOUNT, Filters.attachedTo(Culture.ROHAN, Race.MAN)) {
                        @Override
                        protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                            Set<PhysicalCard> affectedCharacters = new HashSet<>();
                            for (PhysicalCard card : cards)
                                affectedCharacters.add(card.getAttachedTo());
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, Filters.in(affectedCharacters), 3)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
