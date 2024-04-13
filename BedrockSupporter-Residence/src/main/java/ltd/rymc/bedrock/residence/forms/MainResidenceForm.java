package ltd.rymc.bedrock.residence.forms;

import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.forms.select.ResidenceCreateSelectForm;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceSettingSelectForm;
import ltd.rymc.bedrock.residence.forms.teleport.ResidenceTeleportForm;
import ltd.rymc.bedrock.residence.forms.tools.ResidenceToolsForm;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;

public class MainResidenceForm extends RSimpleForm {
    public MainResidenceForm(Player player, RForm previousForm, Language language) {
        super(player, previousForm, language);

        Language.Section main = section("forms.main");
        Language.Section buttons = main.section("buttons");

        title(main.text("title"));
        content(main.text("content"));
        buttons(
                buttons.text("teleport"),
                buttons.text("manage"),
                buttons.text("create"),
                buttons.text("tool")
        );
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        int id = response.clickedButtonId();
        if (id == 0) new ResidenceTeleportForm(bukkitPlayer,this, language).send();
        else if (id == 1) new ResidenceSettingSelectForm(bukkitPlayer,this, language).send();
        else if (id == 2) new ResidenceCreateSelectForm(bukkitPlayer,this, language).send();
        else if (id == 3) new ResidenceToolsForm(bukkitPlayer,this, language).send();
    }
}
