package ltd.rymc.bedrock.modules;

import ltd.rymc.bedrock.common.annote.DependPlugins;
import ltd.rymc.bedrock.common.annote.ModuleAuthors;
import ltd.rymc.bedrock.common.annote.ModuleName;
import ltd.rymc.bedrock.common.annote.ModuleVersion;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.common.utils.PluginUtils;
import ltd.rymc.bedrock.quickshop.event.ShopClickEvent;
import ltd.rymc.bedrock.quickshop.handler.ShopHandler;
import ltd.rymc.bedrock.quickshop.handler.ShopHandlerHikari;
import ltd.rymc.bedrock.quickshop.handler.ShopHandlerNormal;
import ltd.rymc.bedrock.quickshop.listener.ShopListener;
import ltd.rymc.bedrock.quickshop.shop.QuickShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@ModuleName("QuickShopForm")
@ModuleVersion("2.2.0")
@ModuleAuthors({ "RENaa_FD" })
@DependPlugins({ "QuickShop|QuickShop-Hikari" })
public class QuickShopForm extends Module {

    private static ShopHandler shopHandler = null;
    private static ShopListener shopListener = null;

    @Override
    public void onEnable() {

        if (PluginUtils.hasPlugin("QuickShop")){
            getLogger().info("Detect: QuickShop");
            shopHandler = new ShopHandlerNormal();
        } else if (PluginUtils.hasPlugin("QuickShop-Hikari")){
            getLogger().info("Detect: QuickShop-Hikari");
            shopHandler = new ShopHandlerHikari();
        } else {
            getLogger().severe("QuickShop is not detected, the module will be disabled!");
            disable();
            return;
        }

        shopListener = new ShopListener();

        // For ShopClickEvent
        Bukkit.getPluginManager().registerEvents(shopHandler, plugin);

        // Main listener
        Bukkit.getPluginManager().registerEvents(shopListener, plugin);

    }

    @Override
    public void onDisable(){

        if (shopHandler != null) {
            HandlerList.unregisterAll(shopHandler);
            shopHandler = null;
        }

        if (shopListener != null) {
            HandlerList.unregisterAll(shopListener);
            shopListener = null;
        }

    }

    public static ShopHandler getShopHandler(){
        return shopHandler;
    }

    public static boolean callShopClickEvent(QuickShop shop, Player player) {
        ShopClickEvent event = new ShopClickEvent(shop, player);
        event.callEvent();
        return event.isCancelled();
    }

}
