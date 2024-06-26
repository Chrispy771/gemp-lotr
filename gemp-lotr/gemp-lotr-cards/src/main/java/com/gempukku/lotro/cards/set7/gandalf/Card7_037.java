package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.PutCardFromHandOnTopOfDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Skirmish: Place 3 cards from hand on top of your draw deck to make a minion skirmishing an unbound
 * companion strength -3. Any Shadow player may place 3 cards from his or her hand on top of his or her draw deck
 * to prevent this.
 */
public class Card7_037 extends AbstractCompanion {
    public Card7_037() {
        super(4, 7, 4, 6, Culture.GANDALF, Race.WIZARD, Signet.THEODEN, "Gandalf", "Manager of Wizards", true);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && game.getGameState().getHand(playerId).size() >= 3) {
            final ActivateCardAction action = new ActivateCardAction(self);
            for (int i = 0; i < 3; i++)
                action.appendCost(
                        new ChooseCardsFromHandEffect(playerId, 1, 1, Filters.any) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                for (PhysicalCard selectedCard : selectedCards) {
                                    action.insertCost(
                                            new PutCardFromHandOnTopOfDeckEffect(selectedCard, false));
                                }
                            }
                        });
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard card) {
                            action.insertEffect(
                                    new PreventableEffect(action,
                                            new AddUntilEndOfPhaseModifierEffect(
                                                    new StrengthModifier(self, card, -3)) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Make " + GameUtils.getFullName(card) + " strength -3";
                                                }
                                            }, GameUtils.getShadowPlayers(game),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(final CostToEffectAction subAction, final String opponentId) {
                                                    return new UnrespondableEffect() {
                                                        @Override
                                                        public boolean isPlayableInFull(LotroGame game) {
                                                            return game.getGameState().getHand(opponentId).size() >= 3;
                                                        }

                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Place 3 cards from hand on top of draw deck";
                                                        }

                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            for (int i = 0; i < 3; i++)
                                                                subAction.appendEffect(
                                                                        new ChooseCardsFromHandEffect(opponentId, 1, 1, Filters.any) {
                                                                            @Override
                                                                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                                for (PhysicalCard selectedCard : selectedCards) {
                                                                                    subAction.insertEffect(
                                                                                            new PutCardFromHandOnTopOfDeckEffect(selectedCard, false));
                                                                                }
                                                                            }
                                                                        });
                                                        }
                                                    };
                                                }
                                            }
                                    ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
