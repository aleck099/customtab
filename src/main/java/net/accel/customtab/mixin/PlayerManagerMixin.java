package net.accel.customtab.mixin;

import net.accel.customtab.ModMain;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow public abstract void sendToAll(Packet<?> packet);

    @Inject(at = @At("HEAD"), method = "updatePlayerLatency")
    public void updatePlayerLatency(CallbackInfo ci) {
        var header = ModMain.MONITOR.applyArguments(ModMain.CONFIG.header);
        var footer = ModMain.MONITOR.applyArguments(ModMain.CONFIG.footer);
        var packet = new PlayerListHeaderS2CPacket(new LiteralText(header), new LiteralText(footer));
        this.sendToAll(packet);
    }
}
