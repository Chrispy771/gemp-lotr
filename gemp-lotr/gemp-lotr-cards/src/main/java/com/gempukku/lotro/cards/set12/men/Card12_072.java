package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Strength: +3
 * Game Text: Bearer must be The Mouth of Sauron. He is fierce. Response: If a [MEN] minion is about to take a wound,
 * remove (3) to prevent that.
 */
public class Card12_072 extends AbstractAttachable {
    public Card12_072() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.MEN, PossessionClass.MOUNT, "Messenger's Mount", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("The Mouth of Sauron");
    }

    @Override
    public int getStrength() {
        return 3;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.MEN, CardType.MINION)
                && game.getGameState().getTwilightPool() >= 3) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (PreventableCardEffect) effect, playerId, "Choose minion to prevent wound to", Culture.MEN, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
