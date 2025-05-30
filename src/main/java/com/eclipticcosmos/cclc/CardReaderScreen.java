package com.eclipticcosmos.cclc;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class CardReaderScreen extends EasyMenuScreen<CardReaderMenu> {

    public static final ResourceLocation GUI_TEXTURE = VersionUtil.lcResource("textures/gui/container/coin_chest.png");

    public final BlockEntityCardReader be;

    private final ScreenPosition INFO_WIDGET_POSITION = ScreenPosition.of(TraderMenu.SLOT_OFFSET + 160, 236 - 96);

    public CardReaderScreen(CardReaderMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        this.be = this.menu.be;
        this.menu.AddExtraHandler(this::handleClientMessage);
        this.resize(176, 243);
    }

    private void handleClientMessage(io.github.lightman314.lightmanscurrency.api.network.LazyPacketData message) {

    }

    @Override
    protected void initialize(ScreenArea screenArea) {

    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);

        // Draw backgrounds behind active slots
        for (Slot s : this.menu.slots) {
            if (s.isActive())
                gui.blit(GUI_TEXTURE, s.x - 1, s.y - 1, 176, 0, 18, 18);
        }

        // Text titles
        gui.drawString(this.title, 8, 6, 0x404040);
        gui.drawString(this.playerInventoryTitle, 8, this.getYSize() - 94, 0x404040);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        super.renderAfterWidgets(gui);

        MoneyValue balance = this.menu.getStoredBalance();
        String text = "Balance: " + balance.getString() /*balance.getUniqueName()*/;
        gui.drawString(text, 8, 80, 0x404040);
    }


}
