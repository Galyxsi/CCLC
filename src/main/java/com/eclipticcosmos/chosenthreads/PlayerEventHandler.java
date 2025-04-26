package com.eclipticcosmos.chosenthreads;

import com.eclipticcosmos.chosenthreads.species.VarynBaseSpecies;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "chosenthreads")
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {

    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();
        String speciesId = VarynHandler.getSpecies(player);

        VarynBaseSpecies species = VarynRegistry.get(speciesId);
        if (species != null)
        {
            species.onPlayerTick(event.getEntity(), event.getEntity().level());
        }
    }

    @SubscribeEvent
    public static void onVillagerTradeOpen(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Villager villager)) return;
        Player player = event.getEntity();
        String speciesId = VarynHandler.getSpecies(player);

        VarynBaseSpecies species = VarynRegistry.get(speciesId);
        if (species != null)
        {
            species.onVillagerTradeOpen(event.getEntity(), event.getEntity().level(), villager);
        }
    }

    @SubscribeEvent
    public static void onPlayerTradeWithVillager(TradeWithVillagerEvent event) {
        Player player = event.getEntity();
        MerchantOffer offer = event.getMerchantOffer();
        AbstractVillager villager = event.getAbstractVillager();
        String speciesId = VarynHandler.getSpecies(player);

        VarynBaseSpecies species = VarynRegistry.get(speciesId);
        if (species != null)
        {
            species.onPlayerTradeWithVillager(event.getEntity(), event.getEntity().level(), villager, offer);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event)
    {
        LivingEntity blocker = event.getEntity();
        DamageSource source = event.getDamageSource();
        float blocked = event.getBlockedDamage();

        if (blocker.getType().equals(EntityType.PLAYER))
        {
            Player player = (Player) event.getEntity();
            String speciesId = VarynHandler.getSpecies(player);

            VarynBaseSpecies species = VarynRegistry.get(speciesId);
            if (species != null)
            {
                species.onShieldBlock(player, event.getEntity().level(), source, blocked);
            }
        }
    }

}
