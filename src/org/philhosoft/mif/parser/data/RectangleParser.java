package org.philhosoft.mif.parser.data;

import org.philhosoft.mif.model.data.MifData;
import org.philhosoft.mif.model.data.Rectangle;
import org.philhosoft.mif.model.parameter.Brush;
import org.philhosoft.mif.model.parameter.Pen;
import org.philhosoft.mif.parser.ParsingContext;
import org.philhosoft.mif.parser.parameter.BrushParser;
import org.philhosoft.mif.parser.parameter.PenParser;

/*
RECT x1 y1 x2 y2
[ PEN (width, pattern, color) ]
[ BRUSH (pattern, forecolor, backcolor) ]
*/
public class RectangleParser extends FourCoordinatesDataParser implements MifDataParser
{
	public static final String KEYWORD = "RECT";

	private PenParser penParser = new PenParser();
	private BrushParser brushParser = new BrushParser();

	@Override
	public String getKeyword()
	{
		return KEYWORD;
	}

	@Override
	public MifData parseData(ParsingContext context)
	{
		parseCoordinates(context);

		Rectangle rectangle = new Rectangle(coordinates1, coordinates2);
		while (context.readNextLine() && parseOption(rectangle, context))
		{}

		return rectangle;
	}

	private boolean parseOption(Rectangle mifRectangle, ParsingContext context)
	{
		if (penParser.canParse(context))
		{
			Pen pen = penParser.parseParameter(context);
			mifRectangle.setPen(pen);
			return true;
		}
		if (brushParser.canParse(context))
		{
			Brush brush = brushParser.parseParameter(context);
			mifRectangle.setBrush(brush);
			return true;
		}
		context.pushBackLine();
		return false;
	}
}
