package ltd.rymc.bedrock.quickshop.handler;

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.event.ShopClickEvent;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import ltd.rymc.bedrock.modules.QuickShopForm;
import ltd.rymc.bedrock.quickshop.shop.QuickShop;
import ltd.rymc.bedrock.quickshop.shop.QuickShopHikari;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;

public class ShopHandlerHikari implements ShopHandler {

    private static final QuickShopAPI api = com.ghostchu.quickshop.QuickShop.getInstance();
    private static final ShopManager manager = api.getShopManager();


    public QuickShop getQuickShop(Block block) {
        return QuickShopHikari.of(getQuickShop0(block));
    }

    private static Shop getQuickShop0(Block block) {
        if (block == null) return null;
        Shop shop = manager.getShop(block.getLocation());
        if (shop != null) return shop;
        BlockData Drops = block.getBlockData();
        if (!(Drops instanceof Directional)) return null;
        return manager.getShop(block.getRelative(((Directional) Drops).getFacing(),-1).getLocation());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void ShopClickEvent(ShopClickEvent event) {
        QuickShop shop = new QuickShopHikari(event.getShop());
        boolean cancelled = QuickShopForm.callShopClickEvent(shop, event.getClicker());
        event.setCancelled(cancelled);
    }

}
