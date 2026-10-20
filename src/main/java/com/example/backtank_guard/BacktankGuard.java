package com.example.backtank_guard;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.List;

@Mod(value = BacktankGuard.MOD_ID, dist = Dist.CLIENT)
public class BacktankGuard{
    public static final String MOD_ID = "backtank_guard";
    private static final Logger LOGGER = LogUtils.getLogger();
    private boolean alwaysSprint = false;

    public BacktankGuard(ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(this::onScreenInit);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onInteract);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void onInteract(InputEvent.InteractionKeyMappingTriggered event) {
        LOGGER.info("BTG use: {} hand: {}", event.isUseItem(), event.getHand());
        HitResult hit = Minecraft.getInstance().hitResult;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (event.isUseItem() && event.getHand() == InteractionHand.MAIN_HAND && isHoldingTargetItem(Minecraft.getInstance().player) && !player.isCrouching() && hit != null && hit.getType() == HitResult.Type.BLOCK) {
            event.setCanceled(true);
            Minecraft.getInstance().gameMode.useItem(player, InteractionHand.MAIN_HAND);
        }
    }

    private void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof ControlsScreen) {
            for (GuiEventListener widget : event.getListenersList()) {
                if (widget instanceof OptionsList list) {
                    Button toggle = Button.builder(sprintText(), b -> {
                        Config.ALWAYS_SPRINT.set(true);
                        Config.ALWAYS_SPRINT.save();
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

    public boolean isHoldingTargetItem(LivingEntity entity) {
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        return mainHand.getItem() instanceof BacktankItem;
    }
}

