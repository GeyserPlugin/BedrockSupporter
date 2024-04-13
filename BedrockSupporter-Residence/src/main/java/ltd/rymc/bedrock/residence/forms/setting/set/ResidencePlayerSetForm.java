package ltd.rymc.bedrock.residence.forms.setting.set;

import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceNoPermissionForm;
import ltd.rymc.bedrock.residence.utils.ResidenceUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResidencePlayerSetForm extends RCustomForm {

    private final String targetPlayer;
    private final ClaimedResidence claimedResidence;
    HashMap<String, FlagPermissions.FlagState> flags;
    List<String> permissionList;

    public ResidencePlayerSetForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, String targetPlayer, Language language) {
        super(player, previousForm, language);
        this.targetPlayer = targetPlayer;
        this.claimedResidence = claimedResidence;
        if (!ResidenceUtils.hasManagePermission(player, claimedResidence) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }


        title(String.format(text("forms.manage.player-set.set.title"), claimedResidence.getName(), targetPlayer));

        flags = ResidenceUtils.getResidencePlayerFlags(player, targetPlayer, claimedResidence);
        permissionList = new ArrayList<>(flags.keySet());

        addPermissionList();
    }

    private void addPermissionList(){

        Language.Section permission = section("forms.permission");

        for (String flagName : permissionList) {
            int flagPermission = ResidenceUtils.flagToInt(flags.get(flagName));
            Flags flag = Flags.getFlag(flagName);

            String description = flag != null ? String.format(permission.text("description"), flag.getDesc()) : "";
            String name = String.format(permission.text("name"), flagName);

            stepSlider(
                    String.format(permission.text("state"), name + description),
                    flagPermission,
                    permission.text("disabled"),
                    permission.text("not-set"),
                    permission.text("enable")
            );
        }
    }


    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        for (int i = 0, permissionListSize = permissionList.size(); i < permissionListSize; i++) {

            String flagName = permissionList.get(i);
            FlagPermissions.FlagState flagState = ResidenceUtils.intToFlag(response.asStepSlider(i));

            if (flagState == flags.get(flagName)) {
                continue;
            }

            claimedResidence.getPermissions().setPlayerFlag(targetPlayer, flagName, flagState);
        }

        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
