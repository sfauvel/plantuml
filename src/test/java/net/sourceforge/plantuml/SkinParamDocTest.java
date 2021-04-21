package net.sourceforge.plantuml;

import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.ugraphic.UStroke;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.sfvl.docformatter.AsciidocFormatter;
import org.sfvl.docformatter.Formatter;
import org.sfvl.doctesting.junitextension.ApprovalsExtension;
import org.sfvl.doctesting.utils.DocWriter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SkinParamDocTest {
    Formatter formatter = new AsciidocFormatter();

    static DocWriter doc = new DocWriter();
    @RegisterExtension
    static ApprovalsExtension extension = new ApprovalsExtension(doc);

    private String getString(Stereotype fooStereotype, SkinParam skinParam, LineParam param) {
        final UStroke thickness = skinParam
                .getThickness(param, fooStereotype);

        final String line = (thickness == null) ? "Is null" : thickness.toString();
        return param.name() + ":" + line;
    }

    private String getValue(Stereotype fooStereotype, SkinParam skinParam, LineParam param) {
        final UStroke thickness = skinParam
                .getThickness(param, fooStereotype);

        final String line = (thickness == null) ? "Is null" : thickness.toString();
        return line;
    }

    @Test
    void testSkinParam(TestInfo testInfo) {

        for (UmlDiagramType umlDiagramType : UmlDiagramType.values()) {

            final Stereotype fooStereotype = new Stereotype("foo");
            final SkinParam skinParam = SkinParam.create(umlDiagramType);

            class Record {
                LineParam lineParam;
                String result;

                public Record(LineParam lineParam, String result) {
                    this.lineParam = lineParam;
                    this.result = result;
                }

                @Override
                public String toString() {
                    return lineParam.name();
                }
            }

            final Map<String, List<Record>> collect = Stream.of(
                    LineParam.values())
                    .map(lineParam -> new Record(
                            lineParam,
                            getValue(fooStereotype, skinParam, lineParam)
                    ))
                    .collect(Collectors.groupingBy(record -> record.result));

            doc.write("", "", "*umlDiagramType*:" + umlDiagramType.name());
            doc.write("", "", collect.entrySet().stream()
                    .map(e -> String.format("*%s for* : %s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("\n")));
        }

    }


}
