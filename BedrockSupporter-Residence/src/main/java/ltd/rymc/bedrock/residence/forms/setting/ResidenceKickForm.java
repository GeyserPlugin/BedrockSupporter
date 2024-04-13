package ltd.rymc.bedrock.residence.forms.setting;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.utils.ArraysUtils;
import ltd.rymc.bedrock.common.utils.InputUtils;
import ltd.rymc.bedrock.common.utils.PlayerUtils;
import ltd.rymc.bedrock.residence.utils.ResidenceUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

import java.util.List;

public class ResidenceKickForm extends RCustomForm {
    private final ClaimedResidence claimedResidence;
    List<Player> players;
    public ResidenceKickForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;
        if (!ResidenceUtils.hasManagePermission(player, claimedResidence) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }
        players = claimedResidence.getPlayersInResidence();

        Language.Section kick = section("forms.manage.kick");

        title(String.format(kick.text("title"), claimedResidence.getName()));
        dropdown(kick.text("dropdown"), generatePlayerNameList());
        input(kick.text("input1"), kick.text("input2"));
    }

    private String[] generatePlayerNameList(){
        String[] playerNameList = ArraysUtils.rotate(PlayerUtils.translateToNameList(players),1);
        playerNameList[0] = text("forms.manage.kick.choose");
        return playerNameList;
    }

    private String getPlayerName(CustomFormResponse response){
        String input = response.asInput(1);
        int dropdown = response.asDropdown(0);

        if (InputUtils.checkInput(input) && !input.trim().contains(" ")) {
            return input;
        }

        if (dropdown > 0) {
            return players.get(dropdown - 1).getName();
        }

        return null;
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String targetPlayer = getPlayerName(response);

        if(targetPlayer == null){
            sendPrevious();
            return;
        }

        ResidenceUtils.kickPlayer(targetPlayer, claimedResidence);

        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
