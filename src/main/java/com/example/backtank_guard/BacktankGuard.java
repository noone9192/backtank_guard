package com.example.backtank_guard;

import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(value = BacktankGuard.MOD_ID, dist = Dist.CLIENT)
public class BacktankGuard {
    public static final String MOD_ID = "backtank_guard";

    public BacktankGuard() {
        NeoForge.EVENT_BUS.addListener(this::onRightClickBlock);
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

