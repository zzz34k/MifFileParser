package org.philhosoft.mif.parser.data;


import org.philhosoft.mif.model.data.Ellipse;
import org.philhosoft.mif.model.data.MifData;
import org.philhosoft.mif.model.parameter.Brush;
import org.philhosoft.mif.model.parameter.Pen;
import org.philhosoft.mif.parser.ParsingContext;
import org.philhosoft.mif.parser.parameter.BrushParser;
import org.philhosoft.mif.parser.parameter.PenParser;


/*
 ELLIPSE x1 y1 x2 y2
 [ PEN (width, pattern, color) ]
 [ BRUSH (pattern, forecolor, backcolor) ]
 */
public class EllipseParser extends FourCoordinatesDataParser implements MifDataParser
{
	public static final String KEYWORD = "ELLIPSE";

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

		Ellipse ellipse = new Ellipse(coordinates1, coordinates2);
		while (context.readNextLine() && parseOption(ellipse, context))
		{}

		return ellipse;
	}

	private boolean parseOption(Ellipse mifEllipse, ParsingContext context)
	{
		if (penParser.canParse(context))
		{
			Pen pen = penParser.parseParameter(context);
			mifEllipse.setPen(pen);
			return true;
		}
		if (brushParser.canParse(context))
		{
			Brush brush = brushParser.parseParameter(context);
			mifEllipse.setBrush(brush);
			return true;
		}
		context.pushBackLine();
		return false;
	}
}
