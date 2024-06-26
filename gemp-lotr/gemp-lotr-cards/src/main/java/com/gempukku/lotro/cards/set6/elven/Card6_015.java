package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Ally • Home 3 • Elf
 * Strength: 8
 * Vitality: 4
 * Site: 3
 * Game Text: At the start of each turn, heal Elrond. Fellowship: Exert Elrond to discard the top card of your
 * draw deck. If it is an [ELVEN] card, you may take it into hand and heal an Elf companion.
 */
public class Card6_015 extends AbstractAlly {
    public Card6_015() {
        super(4, SitesBlock.FELLOWSHIP, 3, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", "Keeper of Vilya", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (final PhysicalCard card : cards)
                                if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                    action.appendEffect(
                                            new PlayoutDecisionEffect(playerId,
                                                    new MultipleChoiceAwaitingDecision(1, "do you want to take " + GameUtils.getCardLink(card) + " into hand and heal an Elf companion?", new String[]{"Yes", "No"}) {
                                                        @Override
                                                        protected void validDecisionMade(int index, String result) {
                                                            if (result.equals("Yes")) {
                                                                action.appendEffect(
                                                                        new PutCardFromDiscardIntoHandEffect(card));
                                                                action.appendEffect(
                                                                        new ChooseAndHealCharactersEffect(action, playerId, Race.ELF, CardType.COMPANION));
                                                            }
                                                        }
                                                    }));
                                }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
