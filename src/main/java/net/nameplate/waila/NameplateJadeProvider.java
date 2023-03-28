package net.nameplate.waila;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nameplate.Nameplate;
import net.nameplate.access.MobEntityAccess;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum NameplateJadeProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public Identifier getUid() {
        return Nameplate.MOB_LEVEL_INFO;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (((MobEntityAccess) accessor.getEntity()).showMobRpgLabel()) {
            tooltip.append(0, IElementHelper.get().text(Text.translatable("text.nameplate.jade.level", String.valueOf("§e" + ((MobEntityAccess) accessor.getEntity()).getMobRpgLevel()))));
        }
    }

}
