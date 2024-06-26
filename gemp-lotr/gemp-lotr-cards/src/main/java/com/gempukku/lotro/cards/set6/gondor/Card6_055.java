package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Game Text: Bearer must be Aragorn. At the start of each of your turns, you may spot a culture token to heal
 * a companion of that culture.
 */
public class Card6_055 extends AbstractAttachableFPPossession {
    public Card6_055() {
        super(0, 0, 0, Culture.GONDOR, CardType.ARTIFACT, PossessionClass.RING, "Ring of Barahir", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.aragorn;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            final Set<Culture> cultureTokens = new HashSet<>();
            for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.hasAnyCultureTokens(1))) {
                Map<Token, Integer> tokens = game.getGameState().getTokens(physicalCard);
                for (Map.Entry<Token, Integer> tokenIntegerEntry : tokens.entrySet()) {
                    if (tokenIntegerEntry.getValue() > 0) {
                        Culture culture = tokenIntegerEntry.getKey().getCulture();
                        if (culture != null)
                            cultureTokens.add(culture);
                    }
                }
            }
            if (cultureTokens.size() > 0) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);

                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION,
                                new Filter() {
                                    @Override
                                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                        return cultureTokens.contains(physicalCard.getBlueprint().getCulture());
                                    }
                                }));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
