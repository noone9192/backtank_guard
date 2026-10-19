package com.example.backtank_guard;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;

import java.util.List;

@Mod(value = BacktankGuard.MOD_ID, dist = Dist.CLIENT)
public class BacktankGuard{
    public static final String MOD_ID = "backtank_guard";
    private static final Logger LOGGER = LogUtils.getLogger();
    private boolean alwaysSprint = false;

    public BacktankGuard(ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(this::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(this::onScreenInit);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof ControlsScreen) {
            for (GuiEventListener widget : event.getListenersList()) {
                if (widget instanceof OptionsList list) {
                    Button toggle = Button.builder(sprintText(), b -> {
                        alwaysSprint = !alwaysSprint;
                        b.setMessage(sprintText());
                            })
                            .width(150)
                            .build();
                    list.addSmall(List.of(toggle));
                }
            }
        }
    }
    private Component sprintText() {
        return Component.literal("Always Sprint: " + (alwaysSprint ? "ON" : "OFF"));
    }
    public void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && alwaysSprint == true && player.input.forwardImpulse > 0) {
            mc.player.setSprinting(true);
        }
    }

    private void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isHoldingTargetItem(event.getEntity()) && !event.getEntity().isShiftKeyDown()) {
            event.setUseItem(TriState.FALSE);
        }
    }

    public boolean isHoldingTargetItem(LivingEntity entity) {
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        return mainHand.getItem() instanceof BacktankItem;
    }
}

