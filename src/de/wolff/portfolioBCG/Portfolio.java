package de.wolff.portfolioBCG;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import processing.core.PApplet;
import processing.core.PImage;
import de.wolff.portfolioBCG.Portfolio.PortfolioArea.SBUCircle;
import de.wolff.portfolioBCG.elements.Abscissa;
import de.wolff.portfolioBCG.elements.CheckBox;
import de.wolff.portfolioBCG.elements.Ordinate;
import de.wolff.portfolioBCG.elements.Scrollbar;
import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;
import de.wolff.portfolioBCG.modells.PeriodBrand;
import de.wolff.portfolioBCG.modells.PeriodSBU;

public class Portfolio extends PApplet {

	private static String dataClass;

	private MarketData data = null;

	private Properties displayProps;
	
	private Properties textProps;

	private CheckBox cb;

	private Scrollbar sb;

	private PortfolioArea area;
	
	private PortfolioInfo info;

	private Manufacture venture;

	private List<Period> periods;

	private Period period = null;

	private Period pperiod = null;

	private boolean checked = false;

	private boolean pchecked = false;

	private PImage star;

	private PImage quest;

	private PImage cow;

	private PImage dog;

	public Portfolio() {
		try {
			Class<?> data = getClass().getClassLoader().loadClass(dataClass);
			Object obj = data.newInstance();
			if (obj instanceof MarketData)
				this.data = (MarketData) obj;
			else {
				throw new Exception("No valid Data");
			}
			displayProps = new PropertiesFile("conf/display.properties");
			textProps = new PropertiesFile("conf/text.properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setup() {
		venture = data.getCompany();
		String title = String.format(textProps.getProperty("frame_title"), venture);
		frame.setTitle(title);
		area = new PortfolioArea();
		info = new PortfolioInfo();

		star = loadImage(displayProps.getProperty("star_image_path"));
		quest = loadImage(displayProps.getProperty("quest_image_path"));
		cow = loadImage(displayProps.getProperty("cow_image_path"));
		dog = loadImage(displayProps.getProperty("dog_image_path"));
		
		int frameW = Integer.parseInt(displayProps.getProperty("frame_width"));
		int frameH = Integer.parseInt(displayProps.getProperty("frame_height"));

		size(frameW, frameH);
		
		// Initialize CheckBox
		int boxX = Integer.parseInt(displayProps.getProperty("box_x"));
		int boxY = Integer.parseInt(displayProps.getProperty("box_y"));
		int boxS = Integer.parseInt(displayProps.getProperty("box_size"));
		PImage hook = loadImage(displayProps.getProperty("hook_image_path"));
		String cbText = textProps.getProperty("checkBox_text");
		
		cb = new CheckBox(this, boxX, boxY, boxS, hook, cbText);
		checked = cb.isCheck();
		pchecked = !checked;

		// Initialize Scrollbar
		periods = data.getPeriods();

		int barX = Integer.parseInt(displayProps.getProperty("scroll_x"));
		int barY = Integer.parseInt(displayProps.getProperty("scroll_y"));
		int barW = Integer.parseInt(displayProps.getProperty("scroll_width"));
		int barH = Integer.parseInt(displayProps.getProperty("scroll_height"));
		int barL = Integer.parseInt(displayProps.getProperty("scroll_loose"));

		sb = new Scrollbar(this, barX, barY, barW, barH, barL, periods.toArray());
	}

	@Override
	public void draw() {
		cb.update();
		sb.update();
		area.update();
		background(255);
		cb.display();
		sb.display();
		area.display();
		info.display();
		pperiod = period;
		pchecked = checked;
	}

	public void dashedLine(float x1, float y1, float x2, float y2, float n) {
		for (int i = 0; i < n; i += 2) {
			float x1a = x1 + (x2 - x1) * i / n;
			float y1a = y1 + (y2 - y1) * i / n;
			float x2a = x1 + (x2 - x1) * (i + 1) / n;
			float y2a = y1 + (y2 - y1) * (i + 1) / n;
			line(x1a, y1a, x2a, y2a);
		}
	}

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

	public class PortfolioArea {

		/**
		 * X-Position where the Portfolio starts
		 */
		private float x1 = Float.parseFloat(displayProps
				.getProperty("portfolio_x"));;

		/**
		 * Y-Position where the Portfolio starts
		 */
		private float y1 = Integer.parseInt(displayProps
				.getProperty("portfolio_y"));;

		/**
		 * Width of the Portfolio
		 */
		private float w1 = Integer.parseInt(displayProps
				.getProperty("portfolio_width"));;

		/**
		 * Height of the Portfolio
		 */
		private float h1 = Integer.parseInt(displayProps
				.getProperty("portfolio_height"));;

		/**
		 * X-Middle of the Portfolio
		 */
		private float xCenter = x1 + w1 / 2;

		/**
		 * Y-Middle of the Portfolio
		 */
		private float yCenter = y1 + h1 / 2;

		/**
		 * Count of dashes for the center cross
		 */
		private int dashs = Integer.parseInt(displayProps
				.getProperty("portfolio_dash_count"));

		/**
		 * Count of dashes for the cross wire
		 */
		private int wdashs = Integer.parseInt(displayProps
				.getProperty("cross_wire_dash_count"));
		
		/**
		 * X-Position of cross wire information
		 */
		private float xInfo = Float.parseFloat(displayProps.getProperty("cross_wire_text_x"));
		
		/**
		 * Y-Position of cross wire information
		 */
		private float yInfo = Float.parseFloat(displayProps.getProperty("cross_wire_text_y"));

		/**
		 * X-start-position of portfolio background
		 */
		private float x2 = Integer.parseInt(displayProps
				.getProperty("portfolio_background_x"));;

		/**
		 * Y-start-position of portfolio background
		 */
		private float y2 = Integer.parseInt(displayProps
				.getProperty("portfolio_background_y"));;

		/**
		 * Width of portfolio background
		 */
		private float w2 = Integer.parseInt(displayProps
				.getProperty("portfolio_background_width"));

		/**
		 * Height of portfolio background
		 */
		private float h2 = Integer.parseInt(displayProps
				.getProperty("portfolio_background_height"));

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
		 * Circle color for SBUs, which are inside the star area.
		 */
		private SBUColor cStar = new SBUColor(
				displayProps.getProperty("sbu_color_star"));
		/**
		 * Circle color for SBUs, which are inside the questionmark area.
		 */
		private SBUColor cQuest = new SBUColor(
				displayProps.getProperty("sbu_color_quest"));

		/**
		 * Circle color for SBUs, which are inside the cash cow area.
		 */
		private SBUColor cCow = new SBUColor(
				displayProps.getProperty("sbu_color_cow"));

		/**
		 * Circle color for SBUs, which are inside the poor dog area.
		 */
		private SBUColor cDog = new SBUColor(
				displayProps.getProperty("sbu_color_dog"));

		/**
		 * Circle color for highlighted SBUs, which are inside the star area.
		 */
		private SBUColor hStar = new SBUColor(
				displayProps.getProperty("sbu_highlight_star"));

		/**
		 * Circle color for highlighted SBUs, which are inside the questionmark area.
		 */
		private SBUColor hQuest = new SBUColor(
				displayProps.getProperty("sbu_highlight_quest"));

		/**
		 * Circle color for highlighted SBUs, which are inside the cash cow area.
		 */
		private SBUColor hCow = new SBUColor(
				displayProps.getProperty("sbu_highlight_cow"));

		/**
		 * Circle color for highlighted SBUs, which are inside the poor dog area.
		 */
		private SBUColor hDog = new SBUColor(
				displayProps.getProperty("sbu_highlight_dog"));

		/**
		 * Initializes the x- and y-Axis  
		 */
		public PortfolioArea() {

			int marker = Integer.parseInt(displayProps
					.getProperty("axis_marker_length"));

			int logBase = Integer.parseInt(displayProps.getProperty("logBase"));
			String xTitle = textProps.getProperty("xAxis_title");
			xAxis = new Abscissa(Portfolio.this, x1, y1 + h1, w1, marker,
					logBase, xTitle);

			int range = Integer.parseInt(displayProps
					.getProperty("yAxis_range"));
			float target = data.getTargetGrowth();
			String yTitle = textProps.getProperty("yAxis_title");
			
			yAxis = new Ordinate(Portfolio.this, x1, y1, h1, marker, target,
					range, yTitle);

		}

		public void update() {
			over = mouseOver();
			yAxis.update();
			xAxis.update();
			createSBUCircles();
			for (SBUCircle sbu : sbuCircles) {
				sbu.update();
			}
		}

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
					share = xAxis.getRelation(mouseX);
					market = yAxis.getMarketGrowth(mouseY);
				} else {
					dashedLine(hSBU.xpos, y1, hSBU.xpos, y1 + h1, wdashs);
					dashedLine(x1, hSBU.ypos, x1 + w1, hSBU.ypos, wdashs);
					share = xAxis.getRelation(hSBU.xpos);
					market = yAxis.getMarketGrowth(hSBU.ypos);
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

		private void createSBUCircles() {
			int area = sb.getActualArea();
			period = periods.get(area - 1);
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
			checked = cb.isCheck();
			if ((pchecked != checked || !period.equals(pperiod)) && checked
					&& area > 1) {
				Period pp = periods.get(area - 2);
				List<PeriodSBU> preSBU = pp.getSBUs();
				for (SBUCircle c : sbuCircles) {
					int index = preSBU.indexOf(c.getSBU());
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
		}

		public class SBUCircle {

			private PeriodSBU sbu;

			private float radius;

			private float xpos;

			private float ypos;

			private SBUCircle previous = null;

			private float opacity = Integer.parseInt(displayProps
					.getProperty("sbu_opacity"));

			public SBUCircle(float wholeTurnover, PeriodSBU sbu) {
				this.sbu = sbu;
				int n = Integer.parseInt(displayProps
						.getProperty("sbu_radius_factor"));

				radius = sbu.getOwnMarketShare() / wholeTurnover * n;

				xpos = xAxis.marketSharePosition(sbu.getOwnMarketShare(),
						sbu.getRivalMarketShare());

				ypos = yAxis.marketGrowthPosition(sbu.getMarketGrowth());
			}

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

			public boolean mouseOver() {
				if (pow((mouseX - xpos), 2) > pow(radius, 2)) {
					return previous != null && previous.mouseOver();
				}
				if (pow((mouseY - ypos), 2) > pow(radius, 2)) {
					return previous != null && previous.mouseOver();
				}
				return true;
			}

			public PeriodSBU getSBU() {
				return sbu;
			}

			public void setPrevious(SBUCircle previous) {
				if (previous != null) {
					int b = Integer.parseInt(displayProps
							.getProperty("sbu_opacity_denominator"));
					previous.opacity = opacity / b;
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

		public class SBUColor {

			private float r;

			private float g;

			private float b;

			public SBUColor(String prop) {
				String[] values = prop.split(",");
				r = Float.parseFloat(values[0]);
				g = Float.parseFloat(values[1]);
				b = Float.parseFloat(values[2]);
			}

		}
	}
	
	public class PortfolioInfo{
		
		private float x = Float.parseFloat(displayProps.getProperty("text_x_pos"));
		
		private float h1pos = Float.parseFloat(displayProps.getProperty("header1_pos"));
		
		private float h2pos = Float.parseFloat(displayProps.getProperty("header2_pos"));
		
		private float ownPos = Float.parseFloat(displayProps.getProperty("text_pos_own_brands"));
		
		private float s1 = Float.parseFloat(displayProps.getProperty("pos_separator1"));
		
		private float s2 = Float.parseFloat(displayProps.getProperty("pos_separator2"));
		
		private float sEnd = Float.parseFloat(displayProps.getProperty("end_separator"));
		
		private float sDashs = Float.parseFloat(displayProps.getProperty("sep_dash_count"));
		
		private float rivalPos = Float.parseFloat(displayProps.getProperty("text_pos_rival_brands"));
		
		private float sbuList = Float.parseFloat(displayProps.getProperty("text_pos_sbuList"));
		
		private float dist = Float.parseFloat(displayProps.getProperty("entry_distance"));
		
		private int h1Size = Integer.parseInt(textProps.getProperty("header1_size"));
		
		private int h2Size = Integer.parseInt(textProps.getProperty("header2_size"));
		
		private int textSize = Integer.parseInt(textProps.getProperty("text_size"));
		
		private float cText = Float.parseFloat(textProps.getProperty("text_grey"));
		
		private float hText = Float.parseFloat(textProps.getProperty("text_Higlight"));
		
		private String h1 = textProps.getProperty("info_header1");
		
		private String hSBU = textProps.getProperty("sbu_header");
		
		private String brands = textProps.getProperty("brands_of");
		
		private String overview = textProps.getProperty("overview");
		
		public void display(){
			strokeWeight(1);
			fill(cText);
			textSize(h1Size);
			textAlign(LEFT, TOP);
			text(String.format(h1, period), x, h1pos);
			if (area.sSBU != null) {
				PeriodSBU sbu = area.sSBU.getSBU();
				textSize(h2Size);
				text(String.format(hSBU, sbu.toString(),sbu.getMarketGrowth()), x, h2pos);
				
				StringBuilder ownBrands = new StringBuilder();
				for (PeriodBrand brand : sbu.getOwnBrands()) {
					ownBrands.append("\n\u2192\t" + brand);
				}
				String own = String.format(brands, venture, ownBrands.toString(), sbu.getOwnMarketShare());

				StringBuilder rivalBrands = new StringBuilder();
				for (PeriodBrand brand : sbu.getRivalBrands()) {
					rivalBrands.append("\n\u2192" + brand);
				}
				String rival = String.format(brands, sbu.getRival(), rivalBrands.toString(), sbu.getRivalMarketShare());

				textSize(textSize);
				dashedLine(x, s1, sEnd, s1, sDashs);
				text(own, x, ownPos);
				dashedLine(x, s2, sEnd, s2, sDashs);
				text(rival, x, rivalPos);
			} else {
				textSize(h2Size);
				text(overview, x, h2pos);
				for (int i = 0; i < area.sbuCircles.size(); i++) {
					SBUCircle sbu = area.sbuCircles.get(i);
					if (area.hSBU == sbu) {
						fill(hText);
					} else {
						fill(cText);
					}
					textSize(textSize);
					text("\u2192 " + sbu, x, sbuList + i * dist);
				}
			}
		}
		
	}

	public static void analyseData(String dataClass, String[] args) {
		Portfolio.dataClass = dataClass;
		PApplet.main(Portfolio.class.getName(), args);
	}
}
