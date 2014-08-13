package de.wolff.portfolioBCG;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import de.wolff.portfolioBCG.Portfolio.PortfolioArea.SBUCircle;
import de.wolff.portfolioBCG.Settings.Color;
import de.wolff.portfolioBCG.elements.Abscissa;
import de.wolff.portfolioBCG.elements.CheckBox;
import de.wolff.portfolioBCG.elements.Ordinate;
import de.wolff.portfolioBCG.elements.Scrollbar;
import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;
import de.wolff.portfolioBCG.modells.PeriodBrand;
import de.wolff.portfolioBCG.modells.PeriodSBU;

/**
 * Class drawing the portfolio with the proccessing core engine.
 * 
 * @author Luis
 * 
 */
public class Portfolio extends PApplet {

	/**
	 * Full name of the implementation of MarketData
	 */
	private static String dataClass;

	/**
	 * Instance of MarketData, for which a portfolio has to be created
	 */
	private MarketData data = null;

	/**
	 * Instance of settings, which hold the display configuration
	 */
	private Settings displayProps;

	/**
	 * Instance of settings, which hold the text to be written
	 */
	private Settings textProps;

	/**
	 * CheckBox that specifies whether to display the previous period.
	 */
	private CheckBox cb;

	/**
	 * Scrollbar that chooses which period has to be drawn.
	 */
	private Scrollbar sb;

	/**
	 * Area in which the portfolio has to be drawn.
	 */
	private PortfolioArea area;

	/**
	 * Area in which the informations has to be displayed
	 */
	private PortfolioInfo info;

	/**
	 * Venture to analyze
	 */
	private Manufacture venture;

	/**
	 * List of periods to analyze.
	 */
	private List<Period> periods;

	/**
	 * Period for which a portfolio is drawn in the current Draw-call.
	 */
	private Period period = null;

	/**
	 * Period for which a portfolio was drawen in the last Draw-call.
	 */
	private Period pperiod = null;

	/**
	 * Indicates whether the check-Box is activated for the current Draw-call.
	 */
	private boolean checked = false;

	/**
	 * Indicates whether the check-Box was activated in the last Draw-call.
	 */
	private boolean pchecked = false;

	/**
	 * Loads an object of the marketData class and the Settings. Path to
	 * Settings is hard coded.
	 */
	public Portfolio() {
		try {
			Class<?> data = getClass().getClassLoader().loadClass(dataClass);
			Object obj = data.newInstance();
			if (obj instanceof MarketData)
				this.data = (MarketData) obj;
			else {
				throw new Exception("No valid Data");
			}
			displayProps = new Settings("conf/display.properties");
			textProps = new Settings("conf/text.properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates all the objects, which are necessary to draw and interact with
	 * the portfolio.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	@Override
	public void setup() {
		venture = data.getCompany();
		String title = textProps.valueFormated("frame_title", venture);
		frame.setTitle(title);
		area = new PortfolioArea();
		info = new PortfolioInfo();

		int frameW = displayProps.valueAsInt("frame_width");
		int frameH = displayProps.valueAsInt("frame_height");

		size(frameW, frameH);

		// Initialize CheckBox
		int boxX = displayProps.valueAsInt("box_x");
		int boxY = displayProps.valueAsInt("box_y");
		int boxS = displayProps.valueAsInt("box_size");
		PImage hook = loadImage(displayProps.value("hook_image_path"));
		String cbText = textProps.value("checkBox_text");

		cb = new CheckBox(this, boxX, boxY, boxS, hook, cbText);
		checked = cb.isCheck();
		pchecked = !checked;

		// Initialize Scrollbar
		periods = data.getPeriods();

		int barX = displayProps.valueAsInt("scroll_x");
		int barY = displayProps.valueAsInt("scroll_y");
		int barW = displayProps.valueAsInt("scroll_width");
		int barH = displayProps.valueAsInt("scroll_height");
		int barL = displayProps.valueAsInt("scroll_loose");

		sb = new Scrollbar(this, barX, barY, barW, barH, barL,
				periods.toArray());
	}

	/**
	 * Calls the update method on all manageable objects and then the respective
	 * display method. At last, it updates the value of
	 * {@link Portfolio#pperiod} and {@link Portfolio#pchecked}.
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	@Override
	public void draw() {

		cb.update();
		sb.update();

		int n = sb.getActualArea();
		period = periods.get(n - 1);
		checked = cb.isCheck();

		area.update();
		background(255);
		cb.display();
		sb.display();
		area.display();
		info.display();
		pperiod = period;
		pchecked = checked;
	}

	/**
	 * Draws a dashed line from the point (x1, y1) to the point (x2, y2) with
	 * the specified number of strokes.
	 * 
	 * @param x1
	 *            x value of the first point
	 * @param y1
	 *            y value of the first point
	 * @param x2
	 *            x value of the second point
	 * @param y2
	 *            y value of the second point
	 * @param n
	 *            number of strokes to be drawn
	 */
	public void dashedLine(float x1, float y1, float x2, float y2, float n) {
		for (int i = 0; i < n; i += 2) {
			float x1a = x1 + (x2 - x1) * i / n;
			float y1a = y1 + (y2 - y1) * i / n;
			float x2a = x1 + (x2 - x1) * (i + 1) / n;
			float y2a = y1 + (y2 - y1) * (i + 1) / n;
			line(x1a, y1a, x2a, y2a);
		}
	}

	/**
	 * Draws an arrow line from the point (x1, y1) to the point (x2, y2). The
	 * angle value of the arrow head strokes and her size are hard coded.
	 * 
	 * @param x1
	 *            x value of the first point
	 * @param y1
	 *            y value of the first point
	 * @param x2
	 *            x value of the second point
	 * @param y2
	 *            y value of the second point
	 */
	public void arrowline(float x1, float y1, float x2, float y2) {
		float arrowAngle = radians(30);
		float arrowhead = 10;
		line(x1, y1, x2, y2);
		float deltaX = (x2 - x1);
		float deltaY = (y2 - y1);
		float angle = deltaY / sqrt(deltaX * deltaX + deltaY * deltaY);
		angle = asin(angle);
		if (deltaX < 0) {
			angle = -angle + PI;
		}
		line(x2, y2, x2 - arrowhead * cos(angle - arrowAngle), y2 - arrowhead
				* sin(angle - arrowAngle));
		line(x2, y2, x2 - arrowhead * cos(angle + arrowAngle), y2 - arrowhead
				* sin(angle + arrowAngle));
	}

	/**
	 * This class draws the result of the portfolio analysis
	 * 
	 * @author Luis
	 * 
	 */
	public class PortfolioArea {

		/**
		 * X-Position where the portfolio starts
		 */
		private float x1 = displayProps.valueAsFloat("portfolio_x");

		/**
		 * Y-Position where the portfolio starts
		 */
		private float y1 = displayProps.valueAsFloat("portfolio_y");

		/**
		 * Width of the portfolio
		 */
		private float w1 = displayProps.valueAsFloat("portfolio_width");

		/**
		 * Height of the portfolio
		 */
		private float h1 = displayProps.valueAsFloat("portfolio_height");

		/**
		 * X-Middle of the portfolio
		 */
		private float xCenter = x1 + w1 / 2;

		/**
		 * Y-Middle of the portfolio
		 */
		private float yCenter = y1 + h1 / 2;

		/**
		 * Count of dashes for the center cross
		 */
		private int dashs = displayProps.valueAsInt("portfolio_dash_count");

		/**
		 * Count of dashes for the cross wire
		 */
		private int wdashs = displayProps.valueAsInt("cross_wire_dash_count");

		/**
		 * X-Position of cross wire information
		 */
		private float xInfo = displayProps.valueAsFloat("cross_wire_text_x");

		/**
		 * Y-Position of cross wire information
		 */
		private float yInfo = displayProps.valueAsFloat("cross_wire_text_y");

		/**
		 * X-start-position of portfolio background
		 */
		private float x2 = displayProps.valueAsFloat("portfolio_background_x");

		/**
		 * Y-start-position of portfolio background
		 */
		private float y2 = displayProps.valueAsFloat("portfolio_background_y");

		/**
		 * Width of portfolio background
		 */
		private float w2 = displayProps
				.valueAsFloat("portfolio_background_width");

		/**
		 * Height of portfolio background
		 */
		private float h2 = displayProps
				.valueAsFloat("portfolio_background_height");

		/**
		 * Axis, which gives the x-position of an SBU
		 */
		private Abscissa xAxis;

		/**
		 * Axis, which gives the y-position of an SBU
		 */
		private Ordinate yAxis;

		/**
		 * List of circles, representing the SBUs
		 */
		private ArrayList<SBUCircle> sbuCircles;

		/**
		 * SBU which is hovered
		 */
		private SBUCircle hSBU = null;

		/**
		 * SBU which is selected
		 */
		private SBUCircle sSBU = null;

		/**
		 * Indicates whether the mouse pointer hovers the portfolio
		 */
		private boolean over;

		/**
		 * Factor to control the size of circle radius
		 */
		private int cFactor = displayProps.valueAsInt("sbu_radius_factor");

		/**
		 * Denominator of the opacity for strategic business units of the
		 * previous period
		 */
		private int opDeno = displayProps.valueAsInt("sbu_opacity_denominator");

		/**
		 * Lower border of circle radius.
		 */
		private float minS = displayProps.valueAsFloat("sbu_min_size");

		/**
		 * Upper border of circle radius.
		 */
		private float maxS = displayProps.valueAsFloat("sbu_max_size");

		/**
		 * Circle color for SBUs, which are inside the star area.
		 */
		private Color cStar = displayProps.valueAsColor("sbu_color_star");
		/**
		 * Circle color for SBUs, which are inside the questionmark area.
		 */
		private Color cQuest = displayProps.valueAsColor("sbu_color_quest");

		/**
		 * Circle color for SBUs, which are inside the cash cow area.
		 */
		private Color cCow = displayProps.valueAsColor("sbu_color_cow");

		/**
		 * Circle color for SBUs, which are inside the poor dog area.
		 */
		private Color cDog = displayProps.valueAsColor("sbu_color_dog");

		/**
		 * Circle color for highlighted SBUs, which are inside the star area.
		 */
		private Color hStar = displayProps.valueAsColor("sbu_highlight_star");

		/**
		 * Circle color for highlighted SBUs, which are inside the questionmark
		 * area.
		 */
		private Color hQuest = displayProps.valueAsColor("sbu_highlight_quest");

		/**
		 * Circle color for highlighted SBUs, which are inside the cash cow
		 * area.
		 */
		private Color hCow = displayProps.valueAsColor("sbu_highlight_cow");

		/**
		 * Circle color for highlighted SBUs, which are inside the poor dog
		 * area.
		 */
		private Color hDog = displayProps.valueAsColor("sbu_highlight_dog");

		/**
		 * Background image for the star area.
		 */
		private PImage star;

		/**
		 * Background image for the questionmark area.
		 */
		private PImage quest;

		/**
		 * Background image for the cash cow area
		 */
		private PImage cow;

		/**
		 * Background image for the poor dog area.
		 */
		private PImage dog;

		/**
		 * Initializes the x- and y-Axis
		 */
		public PortfolioArea() {

			star = loadImage(displayProps.value("star_image_path"));
			quest = loadImage(displayProps.value("quest_image_path"));
			cow = loadImage(displayProps.value("cow_image_path"));
			dog = loadImage(displayProps.value("dog_image_path"));

			int m = displayProps.valueAsInt("axis_marker_length");

			int logBase = displayProps.valueAsInt("logBase");
			int xmc = displayProps.valueAsInt("xAxis_marker_count");
			String xTitle = textProps.value("xAxis_title");
			xAxis = new Abscissa(Portfolio.this, x1, y1 + h1, w1, m, xmc,
					logBase, xTitle);

			int ymc = displayProps.valueAsInt("yAxis_marker_count");
			int range = displayProps.valueAsInt("yAxis_range");
			float target = data.getTargetGrowth();
			String yTitle = textProps.value("yAxis_title");

			yAxis = new Ordinate(Portfolio.this, x1, y1, h1, m, ymc, target,
					range, yTitle);

		}

		/**
		 * First, checks if the mouse hovers the portfolio area and sets the
		 * {@link PortfolioArea#over} value. Than updates the list
		 * {@link PortfolioArea#sbuCircles} if period has changed. If the check
		 * box was checked and exists an previous period, than updates the
		 * {@link SBUCircle#previous} values. Otherwise set it to null. At last
		 * calls the update method on each entries of the list
		 * {@link PortfolioArea#sbuCircles}
		 */
		public void update() {
			over = mouseOver();

			List<PeriodSBU> sbus = period.getSBUs();
			if (!period.equals(pperiod)) {
				sbuCircles = new ArrayList<>(sbus.size());
				for (PeriodSBU s : sbus) {
					sbuCircles
							.add(new SBUCircle(period.getCompanyTurnover(), s));
				}
				if (!sbuCircles.contains(sSBU)) {
					sSBU = null;
				}
			}
			int n = sb.getActualArea();
			if ((pchecked != checked || !period.equals(pperiod)) && checked
					&& n > 1) {
				Period pp = periods.get(n - 2);
				List<PeriodSBU> preSBU = pp.getSBUs();
				for (SBUCircle c : sbuCircles) {
					int index = preSBU.indexOf(c.sbu);
					if (index == -1) {
						continue;
					}
					c.setPrevious(new SBUCircle(pp.getCompanyTurnover(), preSBU
							.get(index)));
				}
			} else if (!checked) {
				for (SBUCircle c : sbuCircles) {
					c.setPrevious(null);
				}
			}
			for (SBUCircle sbu : sbuCircles) {
				sbu.update();
			}
		}

		/**
		 * Draws the portfolio area and calls the display method on all entries
		 * of the list {@link PortfolioArea#sbuCircles}
		 */
		public void display() {
			// draw Circles
			stroke(0);
			strokeWeight(1);
			fill(250);
			rect(x2, y2, w2, h2);
			xAxis.display();
			yAxis.display();

			float xpos1 = x1 + (xCenter - x1) / 2;
			float ypos1 = y1 + (yCenter - y1) / 2;
			float xpos2 = xCenter + (xCenter - x1) / 2;
			float ypos2 = yCenter + (yCenter - y1) / 2;

			imageMode(CENTER);
			image(star, xpos1, ypos1);
			image(quest, xpos2, ypos1);
			image(dog, xpos2, ypos2);
			image(cow, xpos1, ypos2);

			dashedLine(xCenter, y1, xCenter, y1 + w1, dashs);
			dashedLine(x1, yCenter, x1 + h1, yCenter, dashs);

			// Draw cross wire
			String share, market;
			if (over) {
				strokeWeight((float) 0.5);
				if (hSBU == null) {
					dashedLine(mouseX, y1, mouseX, y1 + h1, wdashs);
					dashedLine(x1, mouseY, x1 + w1, mouseY, wdashs);
					share = xAxis.relationOf(mouseX);
					market = yAxis.growthOf(mouseY);
				} else {
					dashedLine(hSBU.xpos, y1, hSBU.xpos, y1 + h1, wdashs);
					dashedLine(x1, hSBU.ypos, x1 + w1, hSBU.ypos, wdashs);
					share = xAxis.relationOf(hSBU.xpos);
					market = yAxis.growthOf(hSBU.ypos);
				}
			} else {
				share = " - ";
				market = " - ";
			}
			textAlign(LEFT, TOP);
			textSize(8);
			fill(0);
			text("Relativer Markanteil (" + venture + " / Rivale): " + share
					+ " || Marktwachstum: " + market, xInfo, yInfo);

			for (SBUCircle c : sbuCircles) {
				c.display();
			}
		}

		/**
		 * Checks if the mouse hovers the portfolio area.
		 * 
		 * @return whether the portfolio area is hovered
		 */
		public boolean mouseOver() {
			if (mouseX < x1) {
				return false;
			}
			if (mouseX > x1 + w1) {
				return false;
			}
			if (mouseY < y1) {
				return false;
			}
			if (mouseY > y1 + h1) {
				return false;
			}
			return true;
		}

		/**
		 * Class which draws the circle of an strategic business unit
		 * 
		 * @author Luis
		 * 
		 */
		public class SBUCircle {

			/**
			 * The Instance of an strategic business unit for which an circle as
			 * to be drawn.
			 */
			private PeriodSBU sbu;

			/**
			 * The radius of the Circle, which depends from the share of the
			 * total turnover.
			 */
			private float radius;

			/**
			 * X value of circle position.
			 */
			private float xpos;

			/**
			 * Y value of circle position
			 */
			private float ypos;

			/**
			 * An SBUCircle which hold the instance of this strategic business
			 * unit of the previous period.
			 */
			private SBUCircle previous = null;

			/**
			 * Alpha value to use for for the circle.
			 * 
			 * @see PApplet#fill(float, float, float, float)
			 */
			private float opacity = displayProps.valueAsFloat("sbu_opacity");

			/**
			 * Calculates the radius of the circle and the x and y values.
			 * 
			 * @param wholeTurnover
			 *            Turnover of the company in the period to be
			 *            considered. (Attention! This parameter is important,
			 *            because {@link SBUCircle#sbu} can also be of the
			 *            previous period.
			 * @param sbu
			 *            The strategic business unit for which an circle has to
			 *            be drawn.
			 */
			public SBUCircle(float wholeTurnover, PeriodSBU sbu) {
				this.sbu = sbu;

				radius = sbu.getOwnMarketShare() / wholeTurnover;
				radius = radius * cFactor;
				radius = (radius > minS) ? radius : minS;
				radius = (radius < maxS) ? radius : maxS;

				xpos = xAxis.xPosOf(sbu.getOwnMarketShare(),
						sbu.getRivalMarketShare());

				ypos = yAxis.yPosOf(sbu.getMarketGrowth());
			}

			/**
			 * Checks whether the circle is hovered or clicked. If the mouse was
			 * clicked else were and this circle is selected, the selection will
			 * be reset.
			 */
			public void update() {
				boolean over = mouseOver();
				boolean mouse = (mouseButton == LEFT);
				if (over) {
					hSBU = this;
					if (mouse) {
						sSBU = this;
					}
				} else {
					if (equals(sSBU)) {
						if (mouse && getOuterType().over) {
							sSBU = null;
						} else if (this != sSBU) {
							sSBU = this;
						}
					}
				}
				if (!over && hSBU == this) {
					hSBU = null;
				}
			}

			/**
			 * Draw a circle for {@link SBUCircle#sbu} and an arrow to
			 * {@link SBUCircle#previous}, if exists.
			 */
			public void display() {
				strokeWeight(equals(sSBU) ? 3 : 1);
				ellipseMode(RADIUS);
				if (hSBU != null && equals(hSBU)) {
					if (xpos <= xCenter) {
						if (ypos > yCenter)
							fill(hQuest.r, hQuest.g, hQuest.b, opacity);
						else
							fill(hDog.r, hDog.g, hDog.b, opacity);
					} else {
						if (ypos > yCenter)
							fill(hStar.r, hStar.g, hStar.b, opacity);
						else
							fill(hCow.r, hCow.g, hCow.b, opacity);
					}
				} else {
					if (xpos <= xCenter) {
						if (ypos > yCenter)
							fill(cQuest.r, cQuest.g, cQuest.b, opacity);
						else
							fill(cDog.r, cDog.g, cDog.b, opacity);
					} else {
						if (ypos > yCenter)
							fill(cStar.r, cStar.g, cStar.b, opacity);
						else
							fill(cCow.r, cCow.g, cCow.b, opacity);
					}
				}

				ellipse(xpos, ypos, radius, radius);
				// draw previous SBU
				if (previous != null) {
					previous.display();

					strokeWeight(2);
					arrowline(previous.xpos, previous.ypos, xpos, ypos);

					strokeWeight(1);
					float deltaX = (previous.xpos - xpos);
					float deltaY = (previous.ypos - ypos);
					float angle = deltaY
							/ sqrt(deltaX * deltaX + deltaY * deltaY);
					angle = asin(angle) + radians(90);
					if (deltaX < 0) {
						angle = -angle + PI;
					}
					float x11 = previous.xpos + previous.radius * cos(angle);
					float y11 = previous.ypos + previous.radius * sin(angle);
					float x12 = xpos + radius * cos(angle);
					float y12 = ypos + radius * sin(angle);
					dashedLine(x11, y11, x12, y12, 32);

					float x21 = previous.xpos - previous.radius * cos(angle);
					float y21 = previous.ypos - previous.radius * sin(angle);
					float x22 = xpos - radius * cos(angle);
					float y22 = ypos - radius * sin(angle);
					dashedLine(x21, y21, x22, y22, 32);
				}
			}

			/**
			 * Checks if the mouse hovers this circle.
			 * 
			 * @return Whethe the circle is hovered.
			 */
			public boolean mouseOver() {
				if (pow((mouseX - xpos), 2) > pow(radius, 2)) {
					return previous != null && previous.mouseOver();
				}
				if (pow((mouseY - ypos), 2) > pow(radius, 2)) {
					return previous != null && previous.mouseOver();
				}
				return true;
			}

			/**
			 * Sets the SBUCircle for the strategic business unit of the
			 * previous period and calculates his opacity
			 * 
			 * @param previous
			 *            The SBUCircle, which hold the strategic business unit
			 *            of the previous period;
			 */
			public void setPrevious(SBUCircle previous) {
				if (previous != null) {
					previous.opacity = opacity / opDeno;
				}
				this.previous = previous;
			}

			@Override
			public String toString() {
				return sbu.toString();
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result + ((sbu == null) ? 0 : sbu.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				SBUCircle other = (SBUCircle) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (sbu == null) {
					if (other.sbu != null)
						return false;
				} else if (!sbu.equals(other.sbu))
					return false;
				return true;
			}

			private PortfolioArea getOuterType() {
				return PortfolioArea.this;
			}
		}
	}

	/**
	 * Class, which draws the area, were informations for the portfolio has to
	 * be displayed.
	 * 
	 * @author Luis
	 * 
	 */
	public class PortfolioInfo {

		/**
		 * X value, where the information area starts.
		 */
		private float x = displayProps.valueAsFloat("text_x_pos");

		/**
		 * Y value, where the first header has to be displayed.
		 */
		private float h1pos = displayProps.valueAsFloat("header1_pos");

		/**
		 * Y value, where the second header has to be displayed.
		 */
		private float h2pos = displayProps.valueAsFloat("header2_pos");

		/**
		 * Y value, where the brands of {@link Portfolio#venture} has to be
		 * displayed.
		 */
		private float ownPos = displayProps.valueAsFloat("text_pos_own_brands");

		/**
		 * Y value, where the first separator has to be displayed.
		 */
		private float s1 = displayProps.valueAsFloat("pos_separator1");

		/**
		 * Y value, where the second separator has to be displayed.
		 */
		private float s2 = displayProps.valueAsFloat("pos_separator2");

		/**
		 * X value, where all separators end.
		 */
		private float sEnd = displayProps.valueAsFloat("end_separator");

		/**
		 * Number of stroke for the separators
		 */
		private float sDashs = displayProps.valueAsFloat("sep_dash_count");

		/**
		 * Y value, where the brands of the rival venture has to be displayed.
		 */
		private float rivalPos = displayProps
				.valueAsFloat("text_pos_rival_brands");

		/**
		 * Y value, where the list of all strategic business units has to be
		 * displayed.
		 */
		private float sbuList = displayProps.valueAsFloat("text_pos_sbuList");

		/**
		 * Distance between entries of {@link PortfolioInfo#sbuList}.
		 */
		private float dist = displayProps.valueAsFloat("entry_distance");

		/**
		 * Size of first header.
		 */
		private int h1Size = textProps.valueAsInt("header1_size");

		/**
		 * Size of second header
		 */
		private int h2Size = textProps.valueAsInt("header2_size");

		/**
		 * Size of normal text.
		 */
		private int textSize = textProps.valueAsInt("text_size");

		/**
		 * Color of normal text.
		 */
		private Color norm = textProps.valueAsColor("text_grey");

		/**
		 * Color of entries of {@link PortfolioInfo#sbuList}, which contain
		 * strategic business units that are not operated by
		 * {@link Portfolio#venture}.
		 */
		private Color notHolded = textProps.valueAsColor("text_not_holded");

		/**
		 * Color of the hovered SBUCirle.
		 */
		private Color hover = textProps.valueAsColor("text_Higlight");

		/**
		 * Text value of the first header.
		 */
		private String h1 = textProps.value("info_header1");

		/**
		 * Header template for mode showing details of an strategic business
		 * unit.
		 */
		private String hSBU = textProps.value("sbu_header");

		/**
		 * Template for listing Brands of an Company.
		 */
		private String brands = textProps.value("brands_of");

		/**
		 * Text value for die second header for listing all strategic business
		 * units.
		 */
		private String bUnits = textProps.value("business_units");

		/**
		 * Draws the information about the portfolio.
		 */
		public void display() {
			strokeWeight(1);
			fill(norm.r, norm.g, norm.b);
			textSize(h1Size);
			textAlign(LEFT, TOP);
			text(String.format(h1, period), x, h1pos);
			if (area.sSBU != null) {
				PeriodSBU sbu = area.sSBU.sbu;
				textSize(h2Size);
				text(String.format(hSBU, sbu, sbu.getMarketGrowth()), x, h2pos);

				StringBuilder ownBrands = new StringBuilder();
				for (PeriodBrand brand : sbu.getOwnBrands()) {
					ownBrands.append("\n\u2192\t" + brand);
				}
				String own = String.format(brands, venture, ownBrands,
						sbu.getOwnMarketShare());

				StringBuilder rivalBrands = new StringBuilder();
				for (PeriodBrand brand : sbu.getRivalBrands()) {
					rivalBrands.append("\n\u2192" + brand);
				}
				String rival = String.format(brands, sbu.getRival(),
						rivalBrands, sbu.getRivalMarketShare());

				textSize(textSize);
				dashedLine(x, s1, sEnd, s1, sDashs);
				text(own, x, ownPos);
				dashedLine(x, s2, sEnd, s2, sDashs);
				text(rival, x, rivalPos);
			} else {
				textSize(h2Size);
				text(bUnits, x, h2pos);
				for (int i = 0; i < area.sbuCircles.size(); i++) {
					SBUCircle sbu = area.sbuCircles.get(i);
					if (area.hSBU == sbu) {
						fill(hover.r, hover.g, hover.b);
					} else if (sbu.sbu.getOwnMarketShare() <= 0) {
						fill(notHolded.r, notHolded.g, notHolded.b);
					} else {
						fill(norm.r, norm.g, norm.b);
					}
					textSize(textSize);

					text("\u2192 " + sbu, x, sbuList + i * dist);
				}
			}
		}

	}

	/**
	 * Sets the MarketData class to analyse and runs the main method of the
	 * class {@link PApplet}
	 * 
	 * @param dataClass
	 *            Name of MarkedData class, which has to be analyzed
	 * @param args
	 *            Arguments of System
	 * 
	 * @see PApplet#main(String, String[])
	 */
	public static void analyseData(String dataClass, String[] args) {
		Portfolio.dataClass = dataClass;
		PApplet.main(Portfolio.class.getName(), args);
	}
}
