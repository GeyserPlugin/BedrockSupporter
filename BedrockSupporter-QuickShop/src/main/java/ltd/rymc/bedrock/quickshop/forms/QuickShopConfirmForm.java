package ltd.rymc.bedrock.quickshop.forms;

import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.quickshop.shop.QuickShop;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;

public class QuickShopConfirmForm extends RSimpleForm {

    private final QuickShop shop;

    public QuickShopConfirmForm(Player player, RForm previousForm, QuickShop shop) {
        super(player, previousForm, null);
        this.shop = shop;
        title("§l你确定?");
        content("删除商店");
        button("确定", FormImage.of(FormImage.Type.PATH, "textures/ui/realms_green_check.png"));
    }

    @Override
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        if (!(bukkitPlayer.isOp() || shop.getOwner().equals(bukkitPlayer.getUniqueId()))){
            return;
        }

        shop.delete();
    }
}
