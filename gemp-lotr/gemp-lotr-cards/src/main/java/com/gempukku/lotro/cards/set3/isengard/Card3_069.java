package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Saruman may not take wounds during the archery phase and may not be assigned to a skirmish. Assignment:
 * Exert Saruman to assign an [ISENGARD] minion to a companion (except the Ring-bearer). That companion may exert
 * to prevent this.
 */
public class Card3_069 extends AbstractMinion {
    public Card3_069() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", "Servant of the Eye", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new CantTakeWoundsModifier(self,
                        new AndCondition(
                                new PhaseCondition(Phase.ARCHERY),
                                new Condition() {
                                    @Override
                                    public boolean isFullfilled(LotroGame game) {
                                        return !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                                    }
                                }), self));
        modifiers.add(
                new CantBeAssignedToSkirmishModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        },
                        self));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD minion", Culture.ISENGARD, CardType.MINION, Filters.assignableToSkirmishAgainst(Side.SHADOW, Filters.and(CardType.COMPANION, Filters.not(Filters.ringBearer)))) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose non Ring-bearer companion", CardType.COMPANION, Filters.not(Filters.ringBearer), Filters.assignableToSkirmishAgainst(Side.SHADOW, minion)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                                            action.appendEffect(
                                                    new PreventableEffect(
                                                            action,
                                                            new AssignmentEffect(playerId, companion, minion),
                                                            Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                                                            new PreventableEffect.PreventionCost() {
                                                                @Override
                                                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                                    return new ExertCharactersEffect(action, self, companion);
                                                                }
                                                            }
                                                    ));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
