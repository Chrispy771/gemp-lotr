package com.gempukku.lotro.merchant;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardItem;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MerchantService {
    private Merchant _merchant;
    private long _priceGuaranteeExpire = 1000 * 60 * 5;
    private Map<Player, PriceGuarantee> _priceGuarantees = new ConcurrentHashMap<Player, PriceGuarantee>();

    private ReadWriteLock _lock = new ReentrantReadWriteLock(true);
    private Set<CardItem> _merchantableItems = new HashSet<CardItem>();
    private CollectionType _permanentCollection = new CollectionType("permanent", "My cards");
    private CollectionsManager _collectionsManager;

    public MerchantService(LotroCardBlueprintLibrary library, CollectionsManager collectionsManager) {
        _collectionsManager = collectionsManager;
        ParametrizedMerchant parametrizedMerchant = new ParametrizedMerchant();
        parametrizedMerchant.setMerchantSetupDate(new Date());
        _merchant = parametrizedMerchant;

        RarityReader rarityReader = new RarityReader();
        for (int i = 0; i <= 19; i++) {
            SetRarity rarity = rarityReader.getSetRarity(String.valueOf(i));
            for (String blueprintId : rarity.getAllCards())
                _merchantableItems.add(new BasicCardItem(library.getBaseBlueprintId(blueprintId)));
        }
    }

    public Set<CardItem> getSellableItems() {
        return Collections.unmodifiableSet(_merchantableItems);
    }

    public PriceGuarantee priceCards(Player player, Collection<CardItem> cardBlueprintIds) {
        Lock lock = _lock.readLock();
        lock.lock();
        try {
            Date currentTime = new Date();
            Map<String, Integer> buyPrices = new HashMap<String, Integer>();
            Map<String, Integer> sellPrices = new HashMap<String, Integer>();
            for (CardItem cardItem : cardBlueprintIds) {
                Integer buyPrice = _merchant.getCardBuyPrice(cardItem.getBlueprintId(), currentTime);
                if (buyPrice != null)
                    buyPrices.put(cardItem.getBlueprintId(), buyPrice);
                Integer sellPrice = _merchant.getCardSellPrice(cardItem.getBlueprintId(), currentTime);
                if (sellPrice != null)
                    sellPrices.put(cardItem.getBlueprintId(), sellPrice);
            }
            PriceGuarantee priceGuarantee = new PriceGuarantee(sellPrices, buyPrices, currentTime);
            _priceGuarantees.put(player, priceGuarantee);
            return priceGuarantee;
        } finally {
            lock.unlock();
        }
    }

    public void sellCard(Player player, String blueprintId, int price) throws MerchantException {
        Date currentTime = new Date();
        Lock lock = _lock.writeLock();
        lock.lock();
        try {
            PriceGuarantee guarantee = _priceGuarantees.get(player);
            if (guarantee == null || guarantee.getDate().getTime() + _priceGuaranteeExpire < currentTime.getTime())
                throw new MerchantException("Price guarantee has expired");
            Integer guaranteedPrice = guarantee.getSellPrices().get(blueprintId);
            if (guaranteedPrice == null || price != guaranteedPrice)
                throw new MerchantException("Guaranteed price does not match the user asked price");

            boolean success = _collectionsManager.sellCardInPlayerCollection(player, _permanentCollection, blueprintId, price);
            if (!success)
                throw new MerchantException("Unable to remove the sold card from your collection");
        } finally {
            lock.unlock();
        }
    }

    public void buyCard(Player player, String blueprintId, int price) throws MerchantException {
        Date currentTime = new Date();
        Lock lock = _lock.writeLock();
        lock.lock();
        try {
            PriceGuarantee guarantee = _priceGuarantees.get(player);
            if (guarantee == null || guarantee.getDate().getTime() + _priceGuaranteeExpire < currentTime.getTime())
                throw new MerchantException("Price guarantee has expired");
            Integer guaranteedPrice = guarantee.getBuyPrices().get(blueprintId);
            if (guaranteedPrice == null || price != guaranteedPrice)
                throw new MerchantException("Guaranteed price does not match the user asked price");

            boolean success = _collectionsManager.buyCardToPlayerCollection(player, _permanentCollection, blueprintId, price);
            if (!success)
                throw new MerchantException("Unable to remove required currency from your collection");
        } finally {
            lock.unlock();
        }
    }

    private static class BasicCardItem implements CardItem {
        private String _blueprintId;

        private BasicCardItem(String blueprintId) {
            _blueprintId = blueprintId;
        }

        @Override
        public String getBlueprintId() {
            return _blueprintId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BasicCardItem that = (BasicCardItem) o;

            if (_blueprintId != null ? !_blueprintId.equals(that._blueprintId) : that._blueprintId != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return _blueprintId != null ? _blueprintId.hashCode() : 0;
        }
    }

    public static class PriceGuarantee {
        private Map<String, Integer> _sellPrices;
        private Map<String, Integer> _buyPrices;
        private Date _date;

        private PriceGuarantee(Map<String, Integer> sellPrices, Map<String, Integer> buyPrices, Date date) {
            _sellPrices = sellPrices;
            _buyPrices = buyPrices;
            _date = date;
        }

        public Date getDate() {
            return _date;
        }

        public Map<String, Integer> getBuyPrices() {
            return _buyPrices;
        }

        public Map<String, Integer> getSellPrices() {
            return _sellPrices;
        }
    }
}