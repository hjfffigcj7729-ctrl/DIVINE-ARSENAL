package com.divinearsenal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod(DivineArsenal.ID)
public class DivineArsenal {
    public static final String ID = "divinearsenal";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID);
    public static final RegistryObject<Item> LONGINUS = ITEMS.register("longinus", () -> new Weapon(0));
    public static final RegistryObject<Item> BABYLON = ITEMS.register("babylon", () -> new Weapon(1));
    public static final RegistryObject<Item> APOLLO = ITEMS.register("apollo", () -> new Weapon(2));
    static {
        TABS.register("divine_arsenal", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.divinearsenal"))
            .icon(() -> new ItemStack(LONGINUS.get()))
            .displayItems((p, out) -> {
                out.accept(LONGINUS.get());
                out.accept(BABYLON.get());
                out.accept(APOLLO.get());
            }).build());
    }
    public DivineArsenal(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ITEMS.register(bus);
        TABS.register(bus);
        MinecraftForge.EVENT_BUS.register(Attacks.class);
    }
    public static class Weapon extends Item {
        private final int type;
        public Weapon(int type) {
            super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
            this.type = type;
        }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            ItemStack stack = player.getItemInHand(hand);
            if (level instanceof ServerLevel server) {
                if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);
                if (Attacks.ACTIVE.size() >= 12) return InteractionResultHolder.fail(stack);
                HitResult hit = player.pick(100, 0, false);
                Vec3 target = hit.getType() == HitResult.Type.MISS
                    ? player.getEyePosition().add(player.getLookAngle().scale(55))
                    : hit.getLocation();
                target = new Vec3(target.x, Math.max(server.getMinBuildHeight()+4,
                    Math.min(server.getMaxBuildHeight()-15,target.y)), target.z);
                Attacks.ACTIVE.add(new Strike(server,target,type));
                player.getCooldowns().addCooldown(this, type == 1 ? 140 : 200);
                server.playSound(null,BlockPos.containing(target),SoundEvents.END_PORTAL_SPAWN,
                    SoundSource.PLAYERS,2f,type == 2 ? 1.5f : .65f);
                player.displayClientMessage(Component.literal(new String[]{
                    "§c롱기누스의 창 강림", "§6게이트 오브 바빌론 개방", "§e아폴론의 태양 강림"}[type]),true);
            }
            return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
        }
    }
    private static class Strike {
        final ServerLevel world;
        final Vec3 pos;
        final int type;
        int ticks;
        Strike(ServerLevel world,Vec3 pos,int type) {
            this.world=world;this.pos=pos;this.type=type;
        }
    }
    public static class Attacks {
        private static final List<Strike> ACTIVE=new ArrayList<>();
        @SubscribeEvent
        public static void tick(TickEvent.ServerTickEvent event) {
            if(event.phase!=TickEvent.Phase.END) return;
            Iterator<Strike> iter=ACTIVE.iterator();
            while(iter.hasNext()) {
                Strike s=iter.next();
                s.ticks++;
                if(s.type==0) spear(s);
                else if(s.type==1) gates(s);
                else sun(s);
                if(s.ticks>110) iter.remove();
            }
        }
        private static void ring(Strike s,Vec3 c,double radius,int count,boolean flame) {
            for(int i=0;i<count;i++) {
                double a=2*Math.PI*i/count;
                s.world.sendParticles(flame?ParticleTypes.FLAME:ParticleTypes.END_ROD,
                    c.x+Math.cos(a)*radius,c.y,c.z+Math.sin(a)*radius,1,0,0,0,0);
            }
        }
        private static void beam(Strike s,Vec3 a,Vec3 b,int count,boolean flame) {
            for(int i=0;i<=count;i++) {
                Vec3 p=a.lerp(b,i/(double)count);
                s.world.sendParticles(flame?ParticleTypes.FLAME:ParticleTypes.END_ROD,
                    p.x,p.y,p.z,2,.18,.18,.18,.01);
            }
        }
        private static void impact(Strike s,Vec3 p,float power,double radius,float damage) {
            // Non-destructive by default to protect player worlds.
            s.world.explode(null,p.x,p.y,p.z,power,false,Level.ExplosionInteraction.NONE);
            for(LivingEntity e:s.world.getEntitiesOfClass(LivingEntity.class,new AABB(p,p).inflate(radius),
                LivingEntity::isAlive)) {
                if(e.position().distanceToSqr(p)<=radius*radius)
                    e.hurt(s.world.damageSources().magic(),damage);
            }
            s.world.sendParticles(ParticleTypes.EXPLOSION_EMITTER,p.x,p.y,p.z,1,0,0,0,0);
            s.world.playSound(null,BlockPos.containing(p),SoundEvents.GENERIC_EXPLODE,
                SoundSource.PLAYERS,3f,.65f);
        }
        private static void spear(Strike s) {
            Vec3 p=s.pos;
            if(s.ticks<=55) {
                double tip=p.y+Math.max(0,140*(1-s.ticks/55.0));
                for(int i=0;i<70;i++) {
                    double y=tip+i*2.0;
                    if(y>=s.world.getMaxBuildHeight())break;
                    double width=i<12?i*.17:2.1;
                    double a=i*.24+s.ticks*.12;
                    s.world.sendParticles(ParticleTypes.END_ROD,p.x,y,p.z,2,.2,.2,.2,0);
                    s.world.sendParticles(ParticleTypes.FLAME,
                        p.x+Math.cos(a)*width,y,p.z+Math.sin(a)*width,2,.2,.2,.2,.01);
                    s.world.sendParticles(ParticleTypes.FLAME,
                        p.x-Math.cos(a)*width,y,p.z-Math.sin(a)*width,2,.2,.2,.2,.01);
                }
                if(s.ticks%5==0)ring(s,p,8+s.ticks*.3,48,false);
            }
            if(s.ticks==55)impact(s,p,11,32,55);
            if(s.ticks>55&&s.ticks<90&&s.ticks%3==0)
                ring(s,p,10+(s.ticks-55)*2.5,80,true);
        }
        private static void gates(Strike s) {
            Vec3 p=s.pos;
            if(s.ticks<=100&&s.ticks%3==0) {
                int n=s.ticks/3;
                double a=n*2.3999632297;
                double r=5+Math.sqrt(n)*5;
                Vec3 target=p.add(Math.cos(a)*r,0,Math.sin(a)*r);
                Vec3 gate=target.add(0,40,0);
                ring(s,gate,4,36,false);
                ring(s,gate,2.6,28,true);
                if(s.ticks>=18) {
                    beam(s,gate,target,52,false);
                    impact(s,target,2.5f,6,17);
                }
            }
        }
        private static void sun(Strike s) {
            Vec3 p=s.pos;
            if(s.ticks<=65) {
                double y=p.y+Math.max(0,72-s.ticks*1.12);
                double radius=4+Math.min(1,s.ticks/35.0)*14;
                for(int j=-3;j<=3;j++) {
                    double latitude=j*Math.PI/12;
                    ring(s,new Vec3(p.x,y+radius*Math.sin(latitude),p.z),
                        radius*Math.cos(latitude),38,true);
                }
                s.world.sendParticles(ParticleTypes.FLAME,p.x,y,p.z,35,radius*.4,radius*.4,radius*.4,.01);
            }
            if(s.ticks==65)impact(s,p,13,40,70);
            if(s.ticks>65&&s.ticks<100&&s.ticks%3==0)
                ring(s,p,14+(s.ticks-65)*3,96,true);
        }
    }
}
