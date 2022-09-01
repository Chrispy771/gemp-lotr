package com.gempukku.lotro.packs;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import org.junit.Test;

import java.util.List;

public class RarityPackBoxTest extends AbstractAtTest {
    @Test
    public void openingPacks() {
        RarityPackBox fellowshipBox = new RarityPackBox(_library.getSetDefinitions().get("1"));
        for (int i=0; i<10; i++) {
            System.out.println("Pack: "+(i+1));
            final List<CardCollection.Item> items = fellowshipBox.openPack();
            for (CardCollection.Item item : items) {
                System.out.println(item.getBlueprintId());
            }
        }
    }
}
