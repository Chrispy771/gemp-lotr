package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Dwarven Battle Axe
 * Set: Second Edition
 * Side: Free
 * Culture: Possession - Hand Weapon
 * Twilight Cost: 1
 * Type: The One Ring
 * Strength: +1
 * Vitality: +1
 * Card Number: 1U10
 * Game Text: Bearer must be a Dwarf. Each time bearer wins a skirmish, you may discard a card stacked on a [DWARVEN] condition to wound a minion.
 */
public class Card40_010 extends AbstractAttachableFPPossession {
    public Card40_010() {
        super(1, 1, 1, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Dwarven Battle Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}