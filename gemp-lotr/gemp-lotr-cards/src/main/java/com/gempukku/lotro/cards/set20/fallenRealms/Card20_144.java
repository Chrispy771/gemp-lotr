package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Stampede
 * Fallen Realms	Event • Manuever
 * Spot a mounted [FR] man to discard a companion (except the ring-bearer). The Free Peoples player may add (4) to prevent this.
 */
public class Card20_144 extends AbstractEvent {
    public Card20_144() {
        super(Side.SHADOW, 0, Culture.FALLEN_REALMS, "Stampede", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.FALLEN_REALMS, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard a companion (except the ring-bearer)";
                            }
                        },
                        game.getGameState().getCurrentPlayerId(),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                return new AddTwilightEffect(self, 4);
                            }
                        }));
        return action;
    }
}
