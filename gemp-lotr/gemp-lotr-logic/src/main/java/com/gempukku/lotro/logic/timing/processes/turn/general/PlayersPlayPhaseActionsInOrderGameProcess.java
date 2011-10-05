package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsFromStackedVisitor;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsVisitor;

import java.util.LinkedList;
import java.util.List;

public class PlayersPlayPhaseActionsInOrderGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;
    private int _consecutivePasses;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayersPlayPhaseActionsInOrderGameProcess(LotroGame game, PlayOrder playOrder, int consecutivePasses, GameProcess followingGameProcess) {
        _game = game;
        _playOrder = playOrder;
        _consecutivePasses = consecutivePasses;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        Skirmish skirmish = _game.getGameState().getSkirmish();
        if (_game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && (_game.getGameState().getSkirmish().isCancelled()
                || skirmish.getFellowshipCharacter() == null || skirmish.getShadowCharacters().size() == 0)) {
            // If the skirmish is cancelled or one side of the skirmish is missing, no more phase actions can be played
            _nextProcess = _followingGameProcess;
        } else {
            String playerId = _playOrder.getNextPlayer();

            GatherPlayableActionsVisitor visitor = new GatherPlayableActionsVisitor(_game, playerId);
            _game.getGameState().iterateActivableCards(playerId, visitor);

            GatherPlayableActionsFromStackedVisitor stackedVisitor = new GatherPlayableActionsFromStackedVisitor(_game, playerId);
            _game.getGameState().iterateStackedActivableCards(playerId, stackedVisitor);

            List<? extends Action> actions = visitor.getActions();

            List<Action> playableActions = new LinkedList<Action>();
            for (Action action : actions)
                if (_game.getModifiersQuerying().canPlayAction(_game.getGameState(), action))
                    playableActions.add(action);

            for (Action action : stackedVisitor.getActions())
                if (_game.getModifiersQuerying().canPlayAction(_game.getGameState(), action))
                    playableActions.add(action);

            _game.getUserFeedback().sendAwaitingDecision(playerId,
                    new CardActionSelectionDecision(_game, 1, "Choose action to play or Pass", playableActions, true) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Action action = getSelectedAction(result);
                            if (action != null) {
                                _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, 0, _followingGameProcess);
                                _game.getActionsEnvironment().addActionToStack(action);
                            } else {
                                _consecutivePasses++;
                                if (_consecutivePasses >= _playOrder.getPlayerCount())
                                    _nextProcess = _followingGameProcess;
                                else
                                    _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, _consecutivePasses, _followingGameProcess);
                            }
                        }
                    });
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
