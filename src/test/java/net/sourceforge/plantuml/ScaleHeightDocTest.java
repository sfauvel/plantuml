package net.sourceforge.plantuml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.provider.Arguments;
import org.sfvl.docformatter.AsciidocFormatter;
import org.sfvl.docformatter.Formatter;
import org.sfvl.doctesting.junitextension.ApprovalsExtension;
import org.sfvl.doctesting.utils.CodeExtractor;
import org.sfvl.doctesting.utils.DocWriter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class ScaleHeightDocTest {
	Formatter formatter = new AsciidocFormatter();

	static DocWriter doc = new DocWriter();
	@RegisterExtension
	static ApprovalsExtension extension = new ApprovalsExtension(doc);

	Stream<Arguments> data() {
		return Stream.of(
				Arguments.of(50, 50),
				Arguments.of(50, 80),
				Arguments.of(80, 50)
				);
	}

	/**
	 * Scale height scales only on height.
	 * @param testInfo
	 */
	@Test
	@DisplayName(value="Managing scaling diagrams")
	void testScale2(TestInfo testInfo) {
		final String myCode = CodeExtractor.extractPartOfMethod(testInfo.getTestClass().get(), "getString", "1");
		doc.write(formatter.sourceCode(myCode));

		doc.write("",
				"[%autowidth]",
				"[%header]",
				"|====",
				"| width | height | vertical scale",
				data().map(a -> getString(testInfo, (int)a.get()[0], (int)a.get()[1]))
						.collect(Collectors.joining("\n")),
				"|====");
	}

	private String getString(TestInfo testInfo, int width, int height) {
		// >>>1
		final ScaleHeight cut = new ScaleHeight(100);
		final double scale = cut.getScale(width, height);
		// <<<1

		return String.format("| %d | %d | %f", width, height, scale);
	}

}
