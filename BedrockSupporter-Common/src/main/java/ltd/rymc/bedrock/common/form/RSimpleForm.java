package ltd.rymc.bedrock.common.form;

import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.utils.StringUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public abstract class RSimpleForm implements RForm {

    protected SimpleForm.Builder builder;
    protected final Player bukkitPlayer;
    protected final RForm previousForm;
    protected final FloodgatePlayer player;
    protected final Language language;

    public RSimpleForm(Player player, RForm previousForm, Language language){
        this.bukkitPlayer = player;
        this.player = FloodgateApi.getInstance().getPlayer(bukkitPlayer.getUniqueId());
        this.builder = SimpleForm.builder();
        this.previousForm = previousForm;
        this.language = language;
        if (this.player == null) return;
        init();
    }

    public void button(String string){
        builder.button(StringUtils.handleNewLineChar(string));
    }

    public void buttons(String... strings){
        for (String string : strings) {
            button(string);
        }
    }

    public void buttons(ButtonComponent... components){
        for (ButtonComponent component : components) {
            builder.button(component);
        }

    }

    public void content(String content){
        builder.content(StringUtils.handleNewLineChar(content));
    }

    public void title(String title){
        builder.title(title);
    }

    public void button(String string, FormImage image){
        builder.button(StringUtils.handleNewLineChar(string), image);
    }

    private void init(){
        builder.validResultHandler(this::onValidResult);
        builder.closedOrInvalidResultHandler(this::onClosedOrInvalidResult);
    }


    public void onValidResult(SimpleForm form, SimpleFormResponse response){
    }

    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response){
    }

    public void refresh(){
    }

    public void send(){
        if (player == null) return;
        refresh();
        player.sendForm(builder);
    }

    public void sendPrevious(){
        if (player == null || previousForm == null) return;
        previousForm.send();
    }

    public String text(String key) {
        return language == null ? null : language.text(key);
    }

    public Language.Section section(String key) {
        return language == null ? null : language.section(key);
    }

}
