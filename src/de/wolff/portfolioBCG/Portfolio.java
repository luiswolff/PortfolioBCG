package de.wolff.portfolioBCG;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import de.wolff.portfolioBCG.elements.Abscissa;
import de.wolff.portfolioBCG.elements.CheckBox;
import de.wolff.portfolioBCG.elements.Ordinate;
import de.wolff.portfolioBCG.elements.Scrollbar;
import de.wolff.portfolioBCG.modells.Period;
import de.wolff.portfolioBCG.modells.PeriodBrand;
import de.wolff.portfolioBCG.modells.PeriodSBU;

public class Portfolio extends PApplet {

	private static String dataClass;

	private MarketData data = null;
	
	// private SelectOneButtons sob;

	private CheckBox cb;

	private Scrollbar sb;

	private Abscissa xAxis;

	private Ordinate yAxis;

	private ArrayList<SBUCircle> sbuCircles;

	private SBUCircle hoverdCircle = null;

	private SBUCircle selectedCircle = null;

	private float portfolioXcenter;

	private float portfolioYcenter;

	private String we;
	
	private List<Period> periods;

	private Period period = null;

	private Period pperiod = null;

	private boolean checked = false;

	private boolean pchecked = false;
	
	private PImage star;
	
	private PImage questonmark;
	
	private PImage cashCow;
	
	private PImage poorDog;

	public void setup() {
		loadData();
		we = data.getCompany().getName();
		frame.setTitle("Portfolio " + we);
		star = loadImage("star.png");
		questonmark = loadImage("questionmark.png");
		cashCow = loadImage("cash_cow.png");
		poorDog = loadImage("poor_dog.png");
		size(800, 600);

		float growthTarget = data.getTargetGrowth();
		xAxis = new Abscissa(this, 80, 490, 470, 5);
		xAxis.setLogBase(5);
		yAxis = new Ordinate(this, 80, 20, 470, 5);
		yAxis.setMiddle(growthTarget);
		yAxis.setRange(5);

		portfolioXcenter = xAxis.marketSharePosition(1, 1);
		portfolioYcenter = yAxis.marketGrowthPosition(growthTarget);

		// Initialize SelectOneButtons
		// String[] detailgrad = { "Segment", "Marke" };
		// sob = new SelectOneButtons(this, detailgrad, 200, 60, 580, 15, 80);

		// Initialize CheckBox
		PImage hook = loadImage("control/icon-check.png");
		cb = new CheckBox(this, 20, 580, 15, hook);
		checked = cb.isCheck();
		pchecked = !checked;

		// Initialize Scrollbar
		periods = data.getPeriods();
		int periodCount = data.getPeriodsCount();
		String[] periods = new String[periodCount];
		for (int i = 0; i < periodCount; i++){
			periods[i] = this.periods.get(i).toString();
		}
		sb = new Scrollbar(this, 15, 545, 550, 20, periods);
		sb.setLoose(32);
		sb.setLabel(periods);
	}

	@Override
	public void draw() {
		// sob.update();
		cb.update();
		sb.update();
		yAxis.update();
		xAxis.update();
		createSBUCircles();
		for (SBUCircle sbu : sbuCircles) {
			sbu.update();
		}
		background(255);
		// sob.display();
		cb.display();
		sb.display();
		drawPortfolioRect();
		// Draw Portfolio
		for (SBUCircle c : sbuCircles) {
			c.display();
		}
		drawCrossWire();
		drawProtfolioInfo();
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

	private void loadData() {
		try {
			Class<?> data = getClass().getClassLoader().loadClass(dataClass);
			Object obj = data.newInstance();
			if (obj instanceof MarketData)
				this.data = (MarketData) obj;
			else {
				System.err.println("No valid Data");
				System.exit(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void createSBUCircles() {
		int area = sb.getActualArea();
		period = periods.get(area - 1);
		List<PeriodSBU> sbus = period.getSBUs();
		if (!period.equals(pperiod)) {
			sbuCircles = new ArrayList<>(sbus.size());
			for (PeriodSBU s : sbus) {
				sbuCircles.add(new SBUCircle(period.getCompanyTurnover(), s));
			}
			if (!sbuCircles.contains(selectedCircle)){
				selectedCircle = null;
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

	private void drawPortfolioRect() {
		stroke(0);
		strokeWeight(1);
		fill(250);
		rect(15, 15, 550, 520);
		xAxis.display();
		yAxis.display();
		
		float xpos1 = 80 + (portfolioXcenter - 80)/2;
		float ypos1 = 20 + (portfolioYcenter - 20)/2;
		float xpos2 = portfolioXcenter + (portfolioXcenter - 80)/2;
		float ypos2 = portfolioYcenter + (portfolioYcenter - 20)/2;
		
		imageMode(CENTER);
		image(star, xpos1, ypos1);
		image(questonmark, xpos2, ypos1);
		image(poorDog, xpos2, ypos2);
		image(cashCow, xpos1, ypos2);
		
		dashedLine(portfolioXcenter, 20, portfolioXcenter, 490, 128);
		dashedLine(80, portfolioYcenter, 550, portfolioYcenter, 128);
	}

	private void drawCrossWire() {
		String share, market;
		if (hoverPortfolioRect()) {
			strokeWeight((float) 0.5);
			if (hoverdCircle == null) {
				dashedLine(mouseX, 20, mouseX, 490, 64);
				dashedLine(80, mouseY, 550, mouseY, 64);
				share = xAxis.getRelation(mouseX);
				market = yAxis.getMarketGrowth(mouseY);
			} else {
				dashedLine(hoverdCircle.xpos, 20, hoverdCircle.xpos, 490, 64);
				dashedLine(80, hoverdCircle.ypos, 550, hoverdCircle.ypos, 64);
				share = xAxis.getRelation(hoverdCircle.xpos);
				market = yAxis.getMarketGrowth(hoverdCircle.ypos);
			}
		} else {
			share = " - ";
			market= " - ";
		}
		textAlign(LEFT, TOP);
		textSize(8);
		fill(0);
		text("Relativer Markanteil (" + we + " / Rivale): " + share + " || Marktwachstum: " + market, 20, 2);
		
	}

	private boolean hoverPortfolioRect() {
		if (mouseX < 80) {
			return false;
		}
		if (mouseX > 80 + 470) {
			return false;
		}
		if (mouseY < 20) {
			return false;
		}
		if (mouseY > 490) {
			return false;
		}
		return true;
	}

	private void drawProtfolioInfo() {
		strokeWeight(1);
		fill(0);
		textSize(12);
		textAlign(LEFT, TOP);
		text("Periode: " + period, 580, 50);
		if (selectedCircle != null) {
			PeriodSBU sbu = selectedCircle.getSBU();
			text(sbu.toString(), 580, 85);
			text("Mit Wachstum von: " + sbu.getMarketGrowth(), 580, 110);
			StringBuilder ownBrands = new StringBuilder("Marken von " + we);
			for (PeriodBrand brand : sbu.getOwnBrands()) {
				ownBrands.append("\n\u2192\t" + brand);
			}
			ownBrands.append("\nVolumen: " + sbu.getOwnMarketShare());

			StringBuilder rivalBrands = new StringBuilder("Konkurrent: "
					+ sbu.getRival());
			for (PeriodBrand brand : sbu.getRivalBrands()) {
				rivalBrands.append("\n\u2192" + brand);
			}
			rivalBrands.append("\nVolumen: " + sbu.getRivalMarketShare());

			dashedLine(580, 145, 800, 145, 32);
			text(ownBrands.toString(), 580, 150);
			dashedLine(580, 320, 800, 320, 32);
			text(rivalBrands.toString(), 580, 325);
		} else {
			text("Geschäftsfelder:", 580, 85);
			for (int i = 0; i < sbuCircles.size(); i++) {
				SBUCircle sbu = sbuCircles.get(i);
				if (hoverdCircle == sbu) {
					fill(200, 255);
				} else {
					fill(0, 255);
				}
				text("\u2192 " + sbu, 580, 110 + i * 25);
			}
		}
	}

	public class SBUCircle {

		private PeriodSBU sbu;

		private float radius;

		private float xpos;

		private float ypos;

		private float opacity = 127;

		private SBUCircle previous = null;

		public SBUCircle(float wholeTurnover, PeriodSBU sbu) {
			this.sbu = sbu;
			radius = sbu.getOwnMarketShare() / wholeTurnover * 100;
			xpos = xAxis.marketSharePosition(sbu.getOwnMarketShare(),
					sbu.getRivalMarketShare());
			ypos = yAxis.marketGrowthPosition(sbu.getMarketGrowth());
		}

		public void update() {
			boolean over = mouseOver();
			boolean mouse = (mouseButton == LEFT);
			if (over) {
				hoverdCircle = this;
				if (mouse) {
					selectedCircle = this;
				}
			} else {
				if (equals(selectedCircle)) {
					if (mouse && hoverPortfolioRect()) {
						selectedCircle = null;
					} else if (this != selectedCircle){
						selectedCircle = this;
					}
				}
			}
			if (!over && hoverdCircle == this) {
				hoverdCircle = null;
			}
		}

		public void display() {
			float depth;
			if (hoverdCircle != null && equals(hoverdCircle)) {
				depth = 200;
			} else {
				depth = 0;
			}
			strokeWeight(equals(selectedCircle) ? 3 : 1);
			ellipseMode(RADIUS);
			if (xpos <= portfolioXcenter) {
				if (ypos > portfolioYcenter)
					fill(depth, 255, depth, opacity); // Color Questionmarks
				else
					fill(255, 255, depth, opacity); // Color poor dogs
			} else {
				if (ypos > portfolioYcenter)
					fill(depth, depth, 255, opacity);// Color Stars
				else
					fill(255, depth, depth, opacity); // Color cash cows
			}

			ellipse(xpos, ypos, radius, radius);
			if (previous != null) {
				previous.display();

				strokeWeight(2);
				arrowline(previous.xpos, previous.ypos, xpos, ypos);

				strokeWeight(1);
				float deltaX = (previous.xpos - xpos);
				float deltaY = (previous.ypos - ypos);
				float angle = deltaY / sqrt(deltaX * deltaX + deltaY * deltaY);
				angle = asin(angle) + radians(90);
				if (deltaX < 0) {
					angle = -angle + PI;
				}
				dashedLine(previous.xpos + previous.radius * cos(angle),
						previous.ypos + previous.radius * sin(angle), xpos
								+ radius * cos(angle), ypos + radius
								* sin(angle), 32);
				dashedLine(previous.xpos - previous.radius * cos(angle),
						previous.ypos - previous.radius * sin(angle), xpos
								- radius * cos(angle), ypos - radius
								* sin(angle), 32);
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
				previous.opacity = opacity / 3;
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

		private Portfolio getOuterType() {
			return Portfolio.this;
		}
	}

	public static void analyseData(String dataClass, String[] args) {
		Portfolio.dataClass = dataClass;
		PApplet.main(Portfolio.class.getName(), args);
	}
}
