package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class OpponentsCantUseSpecialAbilitiesModifier extends AbstractModifier {
    private final String _playerId;

    public OpponentsCantUseSpecialAbilitiesModifier(PhysicalCard source, String playerId) {
        this(source, null, playerId);
    }

    public OpponentsCantUseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && !performingPlayer.equals(_playerId))
            return false;
        return true;
    }
}
