package com.github.gingjing.plugin.generator.doc.config;

import com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.TreeMap;

import static com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration.TemplateConfig;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @date 2019/08/25
 */
@State(name = "CodeFlutterJavadoc", storages = {@Storage("code-flutter-javadoc.xml")})
public class GenJavadocConfigComponent implements PersistentStateComponent<GenJavadocConfiguration> {

    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    private GenJavadocConfiguration configuration;

    @Nullable
    @Override
    public GenJavadocConfiguration getState() {
        if (configuration == null) {
            configuration = new GenJavadocConfiguration();
            configuration.setAuthor(System.getProperty("user.name"));
            configuration.setDateFormat(DEFAULT_DATE_FORMAT);
            configuration.setSimpleFieldDoc(true);
            configuration.setWordMap(new TreeMap<>());
            configuration.setTranslator("有道翻译");

            TemplateConfig config = new TemplateConfig();
            config.setIsDefault(true);
            config.setTemplate(StringUtils.EMPTY);
            config.setCustomMap(new TreeMap<>());
            configuration.setClassTemplateConfig(config);
            configuration.setMethodTemplateConfig(config);
            configuration.setFieldTemplateConfig(config);
        }
        return configuration;
    }

    @Override
    public void loadState(@NotNull GenJavadocConfiguration state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }
}
