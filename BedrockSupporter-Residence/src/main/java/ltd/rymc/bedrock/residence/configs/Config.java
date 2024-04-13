package ltd.rymc.bedrock.residence.configs;


import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.serialiser.LanguageSerialiser;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.ConfSerialisers;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@SuppressWarnings("unused")
@ConfHeader("# 配置文件 ResidenceForm Config ver 1.0.0 by RENaa_FD")
@ConfSerialisers(LanguageSerialiser.class)
public interface Config {

    @ConfDefault.DefaultString("en_US")
    @ConfKey("language")
    @ConfComments({
            "",
            "# 语言设置,目前可用语言: en_US, zh_CN",
            "# Language settings, currently available languages: en_US, zh_CN"
    })
    @AnnotationBasedSorter.Order(20)
    Language language();

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("hook-residence")
    @ConfComments({
            "",
            "# 将打开表单的指令hook到Residence中",
            "# Hook the command to open the form into Residence"
    })
    @AnnotationBasedSorter.Order(40)
    boolean hookResidence();



}
