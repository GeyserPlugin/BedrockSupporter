package ltd.rymc.bedrock.quickshop.handler;

import ltd.rymc.bedrock.quickshop.shop.QuickShop;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;

public interface ShopHandler extends Listener {
    QuickShop getQuickShop(Block block);

}
