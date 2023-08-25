package com.gempukku.lotro.tournament;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Date;

public class RecurringScheduledQueue extends AbstractTournamentQueue implements TournamentQueue {
    private static final long _signupTimeBeforeStart = 1000 * 60 * 60; // 60 minutes before start

    private final long _repeatEvery;
    private long _nextStart;
    private String _nextStartText;

    private final String _tournamentIdPrefix;
    private final String _tournamentQueueName;

    private final int _minimumPlayers;
    private final TournamentService _tournamentService;

    public RecurringScheduledQueue(long originalStart, long repeatEvery, String tournamentIdPrefix,
                                              String tournamentQueueName, int cost, boolean requiresDeck,
                                              CollectionType collectionType,
                                              TournamentService tournamentService, TournamentPrizes tournamentPrizes, PairingMechanism pairingMechanism, String format, int minimumPlayers) {
        super(cost, requiresDeck, collectionType, tournamentPrizes, pairingMechanism, format);
        _repeatEvery = repeatEvery;
        _tournamentIdPrefix = tournamentIdPrefix;
        _tournamentQueueName = tournamentQueueName;
        _tournamentService = tournamentService;
        _minimumPlayers = minimumPlayers;
        long number = (System.currentTimeMillis() - originalStart) / repeatEvery;

        _nextStart = originalStart + (number + 1) * repeatEvery;
        _nextStartText = DateUtils.formatDateWithHour(new Date(_nextStart));
    }

    @Override
    public String getStartCondition() {
        return _nextStartText;
    }

    @Override
    public String getTournamentQueueName() {
        return _tournamentQueueName;
    }

    @Override
    public String getPairingDescription() {
        return _pairingMechanism.getPlayOffSystem() + ", minimum players: " + _minimumPlayers;
    }

    @Override
    public boolean isJoinable() {
        return System.currentTimeMillis() >= _nextStart - _signupTimeBeforeStart;
    }

    @Override
    public boolean process(TournamentQueueCallback tournamentQueueCallback, CollectionsManager collectionsManager) throws SQLException, IOException {
        long now = System.currentTimeMillis();
        if (now > _nextStart) {
            if (_players.size() >= _minimumPlayers) {
                String tournamentId = _tournamentIdPrefix+System.currentTimeMillis();
                String tournamentName = _tournamentQueueName + " - " + DateUtils.getStringDateWithHour();

                for (String player : _players)
                    _tournamentService.addPlayer(tournamentId, player, _playerDecks.get(player));

                var info = new TournamentInfo(tournamentId, null, tournamentName, _format, ZonedDateTime.now(),
                        _collectionType, Tournament.Stage.PLAYING_GAMES, 0, false,
                        _pairingMechanism, _tournamentPrizes);

                var tournament = _tournamentService.addTournament(info);

                tournamentQueueCallback.createTournament(tournament);

                _players.clear();
                _playerDecks.clear();
            } else {
                leaveAllPlayers(collectionsManager);
            }
            _nextStart+=_repeatEvery;
            _nextStartText = DateUtils.formatDateWithHour(new Date(_nextStart));
        }
        return false;
    }
}