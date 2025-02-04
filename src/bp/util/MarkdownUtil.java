package bp.util;

import java.util.Arrays;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownUtil
{
	public final static String md2html(String text)
	{
		List<Extension> extensions = Arrays.asList(TablesExtension.create());
		Parser parser = Parser.builder().extensions(extensions).build();
		Node document = parser.parse(text);
		HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
		String rc = renderer.render(document);
		return rc;
	}

	public final static String fix_md2html_swing(String html)
	{
		return html.replace("<table>", "<table border='1' cellspacing='0' cellpadding='2' style='border-collapse:collapse'>");
	}

	public final static String fix_md2html_export(String html)
	{
		return "<html>\n<head>\n</head>\n<body>\n" + html.replace("<table>", "<table border='1' cellspacing='0' cellpadding='2' style='border-collapse:collapse'>") + "\n</body>\n</html>";
	}
}