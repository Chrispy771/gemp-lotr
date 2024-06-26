package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Exert a [SAURON] minion to return a Free Peoples condition to its owner's hand (or 2 if both are [SHIRE]
 * conditions).
 */
public class Card10_100 extends AbstractEvent {
    public Card10_100() {
        super(Side.SHADOW, 1, Culture.SAURON, "Speak No More to Me", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION));
        List<Effect> possibleEffects = new LinkedList<>();
        possibleEffects.add(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Return Free Peoples condition to its owner's hand";
                    }
                });
        possibleEffects.add(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 2, 2, Side.FREE_PEOPLE, CardType.CONDITION, Culture.SHIRE) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Return 2 Free Peoples SHIRE conditions to its owner's hand";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
